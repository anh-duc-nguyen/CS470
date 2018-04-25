import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the multi-threading part
 * @author  Minghao Shan, Anh Nguyen, Andrew Rodeghero, Harshavardhan Reddy Madduri
 * @version 04/22/2018
 */
public class ProxyThread extends Thread {
    private final Socket clientSocket;
    private final ConcurrentHashMap<String, String> cache;

    /**
     * Constructor
     * @param s     Socket
     * @param cache Cache
     */
    public ProxyThread(Socket s, ConcurrentHashMap<String, String> cache) {
        clientSocket = s;
        this.cache = cache;
    }

    /**
     * Runs when a new thread starts
     */
    public void run() {
        String req = "", url = "";
        try {
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            String firstLine = line;
            while (!line.isEmpty()) {
                req += line;
                line = reader.readLine();
            }
            //get the header of request
            String header = firstLine.substring(0, firstLine.indexOf(' '));

            url = firstLine.substring(firstLine.indexOf(' ') + 1, firstLine.lastIndexOf(' '));
            //return not implemented error if header is not get
            if (!header.toUpperCase().equals("GET")) {
                clientSocket.getOutputStream().write("HTTP/1.1 501 Not Implemented\r\n\r\n".getBytes());
                throw new Exception("501 Not Implemented");
            }
            //Syntax error
            if (!url.startsWith("http")) {
                clientSocket.getOutputStream().write("HTTP/1.1 400 Bad Request\r\n\r\n".getBytes());
                throw new Exception("400 Bad Request");
            }

            String result = "";
            if (checkCache(url)) {
                System.out.println("Sending from cache");
                result = fetchFromCache(url);
            } else {
                result = getWebpage(url);
            }
            addToCache(url, result);
            System.out.println("Sending " + clientSocket.getInetAddress() + "\n\nResponse:\n" + result);
            clientSocket.getOutputStream().write(result.getBytes());

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        updateCache();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 3000, 3000);

        } catch (Exception e) {
            System.out.println(req);
            System.out.println(url);
            e.printStackTrace();
        }
    }

    /**
     * Checks if request was already stored in cache
     * @param url   The key of cache; the request from client
     * @return      True if the request exist and vice versa
     */
    private boolean checkCache(String url) {
        return cache.containsKey(url);
    }

    /**
     * Add the key value pair to cache
     * @param url       The key of cache; the request from client
     * @param response  The value of cache; the response from host server
     */
    private void addToCache(String url, String response) {
        cache.put(url, response);
    }

    /**
     * Returns the respond corresponding to the url
     * @param url   The request from client
     * @return      The response from host server
     */
    private String fetchFromCache(String url) {
        return cache.get(url);
    }

    private void updateCache() throws IOException {
        String result;
        for(String url:cache.keySet()) {
            result = getWebpage(url);
            addToCache(url, result);
            System.out.println("Cache for " + url +" is updated!");

        }
    }
    /**
     * returns the response from host server
     * @param url   The request from client
     * @return      The response from host server
     */
    private String getWebpage(String url) throws IOException{
        URL result = new URL(url);
        URLConnection yc = result.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()));
        String response = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            response += inputLine;

        return response;
    }

    private String getWebpage(String url, String req) throws IOException {
        Socket socket = new Socket(url, 80);
        socket.getOutputStream().write(req.getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        String response = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null){
            inputLine += "\n";
            response += inputLine;
        }
        return response;
    }

}
