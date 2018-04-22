package proxyNetworkTwo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Proxy extends NetworkEntity {
	private final int TTL = 10;
	private ConcurrentHashMap <String, InetAddress> cache;
	private HashMap<InetAddress,ProxyThread> threadList;
	public Proxy(String name) throws UnknownHostException, SocketException {
		super(name);
		threadList = new HashMap<InetAddress,ProxyThread>();
	}
	public void addCache(String host, InetAddress hostIP){
		cache.put(host, hostIP);
	}
	public void checkHost(String host) throws IOException{
		InetAddress hostIP = cache.get(host);
		sendingPacket(host,hostIP);
		if (! recievingPacket().equals("Online") ){
			cache.remove(hostIP);
		}
	}
	public void reloadCache() throws IOException{
		for(String host: cache.keySet()){
			this.checkHost(host);
		}
	}
	
	public String findHost(String host){
		if (cache.containsKey(host)){
			return cache.get(host).toString();
		}else{
			return "not found, bro";
		}
	}
	
	public boolean newCustomer(InetAddress customerIP){
		return threadList.containsKey(customerIP);
	}
	
	public void newClientConnected(InetAddress clientIP, String clientRequest) throws SocketException{
		Thread newThread = new ProxyThread(clientIP, clientRequest, this.cache);
		threadList.put(clientIP, (ProxyThread) newThread);
	}
	public void runn(){
		for (Thread t : threadList.values() ){
			t.run();
		}
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
	}
}
