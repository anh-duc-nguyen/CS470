package proxyNetworkTwo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Server extends NetworkEntity {
	private HashMap<String, String> map = new HashMap<>();
	private String respondForCustomer;
	
	public Server(String name) throws UnknownHostException, SocketException {
		super(name);
	}
	public void sendingPacket(String message, InetAddress reciever) throws IOException{
		byte[] message_ = message.getBytes();
		//Message message = new Message(myIP, providerIP,request);
		DatagramPacket sendPacket = new DatagramPacket(message_, message_.length,reciever,9999);
        SOCKET.send(sendPacket);
	}
	public DatagramPacket recievingPacket() throws IOException{
		byte[] inMessage = new byte[1024];
		DatagramPacket inPacket = new DatagramPacket(inMessage, inMessage.length);
		SOCKET.receive(inPacket);
		System.out.println(new String(inPacket.getData(),"US-ASCII"));
		return inPacket;
		//return new String(inPacket.getData(),"US-ASCII");
	}
	public void newEntry(String request, String responde){
		map.put(request, responde);
	}
	public void removeEntry(String request){
		map.remove(request);
	}
	public boolean hasRespond(String request){
		if (!map.containsKey(request)){
			respondForCustomer = "Error 404: Page not found";
			return false;
		}else{
			return true;
		}
	}
	public void printMap(){
		System.out.println(map);
	}
	public String getRespond(String request){
		if (this.hasRespond(request)){
			respondForCustomer = map.get(request);
		}
		return respondForCustomer;
	}
}
