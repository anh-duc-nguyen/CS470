package proxyNetwork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is acting as a caching along with being a forwarding agent.
 * @author  Minghao Shan, Anh Nguyen, Andrew Rodeghero, Harshavardhan Reddy Madduri
 * @version 04/22/2018
 */
public class Proxy extends NetworkEntity {
	private final int TTL = 10;
	private ConcurrentHashMap <String, InetAddress> cache;
	private HashMap<InetAddress,ProxyThread> threadList;

    /**
     * Constructor
     * @param name      Name of the proxy
     */
	public Proxy(String name) throws UnknownHostException, SocketException {
		super(name);
		threadList = new HashMap<InetAddress,ProxyThread>();
	}

    /**
     * Add the hostResponse to the cache
     * @param host      The host?????????????????????
     * @param hostIP    The ip address of the host
     */
	public void addCache(String host, InetAddress hostIP){
		cache.put(host, hostIP);
	}

    /**
     * Checks if the host is online????????????????????
     * @param host      The host ??????
     */
	public void checkHost(String host) throws IOException{
		InetAddress hostIP = cache.get(host);
		sendingPacket(host,hostIP);

		//if the host is DISCONNECTED?????, remove the host


		if (! recievingPacket().equals("Online") ){
			cache.remove(hostIP);
		}
	}

    /**
     * Checks every client info in the cache
     */
	public void renewCache() throws IOException{
		for(String host: cache.keySet()){
			this.checkHost(host);
		}
	}

    /**
     * Not sure what it does????????????????????????
     *
     *
     * @param host  The host??????????
     * @return      Could not connect information
     */
    //A host is a String?????????????????????
	public String createHost(String host){
		if (cache.containsKey(host)){
			return cache.get(host).toString();
		}else{
			return "Host not found, bro";
		}
	}

    /**
     * Checks if the ip address of client is new
     * @param clientIP  The ip address of the client
     * @return          True if the client already existed; false if the client is new
     */
	public boolean isNewClient(InetAddress clientIP){
		return threadList.containsKey(clientIP);
	}

    /**
     * Connect new client by creating a thread for it.
     * @param clientIP      The ip address of the client
     * @param clientRequest The request send from client
     */
	public void connectedNewClient(InetAddress clientIP, String clientRequest) throws SocketException{
		Thread newThread = new ProxyThread(clientIP, clientRequest, this.cache);
		threadList.put(clientIP, (ProxyThread) newThread);
	}

    /**
     * Run every thread in the threadList hashmap.
     */
	public void runThreads(){
		for (Thread t : threadList.values() ){
			t.run();
		}
	}

    /**
     *
     * @param message
     * @param reciever
     */
	public void sendingPacket(String message, InetAddress reciever) throws IOException{
		byte[] message_ = message.getBytes();
		//Message message = new Message(myIP, providerIP,request);
		DatagramPacket sendPacket = new DatagramPacket(message_, message_.length,reciever,9999);
        SOCKET.send(sendPacket);
	}

    /**
     * 
     * @return
     */
	public DatagramPacket recievingPacket() throws IOException{
		byte[] inMessage = new byte[1024];
		DatagramPacket inPacket = new DatagramPacket(inMessage, inMessage.length);
		SOCKET.receive(inPacket);
		System.out.println(new String(inPacket.getData(),"US-ASCII"));
		return inPacket;
	}
}
