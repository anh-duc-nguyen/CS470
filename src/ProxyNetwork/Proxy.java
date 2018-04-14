package ProxyNetwork;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Proxy {
	private String name;
	private InetAddress myIPAddress;
	private final DatagramSocket SOCKET;
	public Proxy(String name) throws UnknownHostException, SocketException{
		SOCKET = new DatagramSocket(8888);
		this.myIPAddress = InetAddress.getLocalHost();
		this.name = name;
	}
	public DatagramSocket getSocket(){
		return this.SOCKET;
	}
	public String getName(){
		return this.name;
	}
	public InetAddress getIP(){
		return this.myIPAddress;
	}
	public String toString(){
		return "Proxy Server: "+this.getName()+" IP:" + this.getIP();
	}
	public void closeSocket(){
		SOCKET.close();
	}
	public String printPacket(DatagramPacket aPacket) throws UnsupportedEncodingException{
		return new String(aPacket.getData(),"US-ASCII");
	}
	public void listening(){
		SOCKET.receive(recievePacket);
		System.out.println(printPacket(recievePacket));
	}
}
