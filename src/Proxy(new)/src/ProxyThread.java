import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyThread extends Thread{
	private final Socket clientSocket;
	private final ConcurrentHashMap<String, String> cache;
	
	public ProxyThread(Socket i, ConcurrentHashMap<String, String> cache) {
		clientSocket = i;
		this.cache = cache;
	}
	
	public void run(){
        String req = "", url="";
		try {
			InputStreamReader isr =  new InputStreamReader(clientSocket.getInputStream());
	        BufferedReader reader = new BufferedReader(isr);
	        String line = reader.readLine();        
	        String firstLine = line;
	        while (!line.isEmpty()) {
	            req += line;
	            line = reader.readLine();
	        }
	        String method = firstLine.substring(0, firstLine.indexOf(' '));
	        	url = firstLine.substring(firstLine.indexOf(' ') + 1, firstLine.lastIndexOf(' '));
	        	
	        	if(!method.toUpperCase().equals("GET")) {
	        		throw new Exception(method + " method is not yet implemented!");
	        	}
	        	
	        	if(!url.startsWith("http")) {
	        		throw new Exception("Invalid HTTP request format");
	        	}
	        	String result = "";
	        	if (checkCache(url)) {
	        		System.out.println("Sending from cache");
	        		result = fetchFromCache(url);
	        	}
	        	else {
	        		result = getWebpage(url);
	        	}
	        	addToCache(url, result);
	        	System.out.println("Sending " + clientSocket.getInetAddress() + "\n" + result);
	        	clientSocket.getOutputStream().write(result.getBytes());
	        	
		} catch(Exception e) {
			System.out.println(req);
			System.out.println(url);
			e.printStackTrace();
		}
	}
	
	private boolean checkCache(String url) {
		return cache.containsKey(url);
	}
	
	private void addToCache(String url, String response) {
		cache.put(url, response);
	}
	
	private String fetchFromCache(String url) {
		return cache.get(url);
	}
	
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

}
