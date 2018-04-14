package proxyNetworkTwo;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetworkEntity {
	private String name;
	protected InetAddress myIP;
	protected final DatagramSocket SOCKET;
	public NetworkEntity(String name) throws UnknownHostException, SocketException{
		this.SOCKET = new DatagramSocket(9999);
		this.name = name;
		this.myIP = InetAddress.getLocalHost();
	}
	public String getName(){
		return this.name;
	}
	public InetAddress getIP(){
		return this.myIP;
	}
	public DatagramSocket getSocket(){
		return this.SOCKET;
	}
	public String toString(){
		return this.getName() + ": "+this.getIP();
	}
}
