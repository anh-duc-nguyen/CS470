package proxyNetworkTwo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Client extends NetworkEntity {
	private InetAddress providerIP;
	private String request;
	private byte[] message;
	public Client(String name) throws UnknownHostException, SocketException {
		super(name);
	}
	public void setProvider(String provider) throws UnknownHostException{
		this.providerIP = InetAddress.getByName(provider);
	}
	public void setRequest(String request){
		this.request = request;
		this.message = request.getBytes();
	}
	public String getRequest(){
		return this.request;
	}
	public void sendingPacket() throws IOException{
		//Message message = new Message(myIP, providerIP,request);
		DatagramPacket sendPacket = new DatagramPacket(message, message.length,providerIP,9999);
        SOCKET.send(sendPacket);
	}
	public void recievingPacket() throws IOException{
		byte[] inMessage = new byte[1024];
		DatagramPacket inPacket = new DatagramPacket(inMessage, inMessage.length);
		SOCKET.receive(inPacket);
		System.out.println(new String(inPacket.getData(),"US-ASCII"));
	}
}
