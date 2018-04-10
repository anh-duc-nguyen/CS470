package ProxyNetwork;
import java.io.IOException;
import java.net.*;

public class Client {
	private String name;
	private InetAddress myIPAddress;
	private InetAddress providerIP;
	private byte[] request;
	private final DatagramSocket SOCKET;
	public Client(String name) throws UnknownHostException, SocketException{
		SOCKET = new DatagramSocket(9999);
		this.myIPAddress = InetAddress.getLocalHost();
		this.name = name;
	}
	public void setRequest(byte[] request){
		this.request = request;
	}
	public void setProvider( InetAddress providerIP){
		this.providerIP = providerIP;
	}
	public InetAddress getProvider(){
		return this.providerIP;
	}
	public byte[] getRequest(){
		return this.request;
	}
	public String getName(){
		return this.name;
	}
	public InetAddress getIP(){
		return this.myIPAddress;
	}
	public String toString(){
		return "Client: "+ this.getName() + " IP:" + this.getIP() +" "+ this.getRequest()+"\n"
				+"Currently connect to: "+this.getProvider();
	}
	public void sendingPacket() throws IOException{
		Message message = new Message(myIPAddress, providerIP,request);
		DatagramPacket sendPacket = new DatagramPacket(message.getMessager(), message.getLength(), providerIP, 9876);
        SOCKET.send(sendPacket);
	}
	public void recievingPacket() throws IOException{
		byte[] messager = new byte[1024];
		DatagramPacket recievePacket = new DatagramPacket(messager, messager.length);
		SOCKET.receive(recievePacket);
	}
}
