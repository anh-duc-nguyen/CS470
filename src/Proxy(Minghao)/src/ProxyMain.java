import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the main program for the proxy
 * @author  Minghao Shan, Anh Nguyen, Andrew Rodeghero, Harshavardhan Reddy Madduri
 * @version 04/22/2018
 */
public class ProxyMain {
	
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();
		
		System.out.println("Listening on port: " + 8888);
		while(true) {
			Socket clientSocket = server.accept();
            ProxyThread pThread = new ProxyThread(clientSocket, cache);
            pThread.run();
		}
	}

}
