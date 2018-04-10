package ProxyNetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

public class Server {
	private String name;
	private InetAddress myIPAddress;
	private Set<String> httpAddress;
	public Server(String name) throws UnknownHostException{
		this.name = name;
		this.myIPAddress = InetAddress.getLocalHost();
	}
	public void getHTTPFromFile(String filePath) throws IOException{
		File file = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String anAddress;
		while ((anAddress = br.readLine()) != null)
		{
			this.httpAddress.add(anAddress);
		}
		br.close();
	}
	public String getName(){
		return this.name;
	}
	public InetAddress getIP(){
		return this.myIPAddress;
	}
	public String toString(){
		return "Server: " + this.getName() +" IP:" + this.getIP();
	}
}

