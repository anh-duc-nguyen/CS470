package proxyNetwork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Receives the request from proxy/client, and return a response according to the request
 * @author  Minghao Shan, Anh Nguyen, Andrew Rodeghero, Harshavardhan Reddy Madduri
 * @version 04/22/2018
 */
public class Server extends NetworkEntity {
	private HashMap<String, String> map = new HashMap<>();
	private String responseForCustomer;

    /**
     * Constructor
     * @param name  The name of the server
     */
	public Server(String name) throws UnknownHostException, SocketException {
		super(name);
	}

    /**
     * Receiving the paccket from the proxy/client
     * @return  the packet from the proxy/client
     */
	public DatagramPacket recievingPacket() throws IOException{
		byte[] inMessage = new byte[1024];
		DatagramPacket inPacket = new DatagramPacket(inMessage, inMessage.length);
		SOCKET.receive(inPacket);
		System.out.println(new String(inPacket.getData(),"US-ASCII"));
		return inPacket;
		//return new String(inPacket.getData(),"US-ASCII");
	}

    /**
     * Create entry where response is corresponding with request
     * @param request   The request from proxy/client
     * @param response  The response for that response
     */
	public void createEntry(String request, String response){
		map.put(request, response);
	}

    /**
     * Remove entry where response is corresponding with request
     * @param request   The request from proxy/client
     */
	public void removeEntry(String request){
		map.remove(request);
	}

    /**
     * Checks if the request have a response.
     * @param request   Request from proxy/client
     * @return          True if the the request have a response, otherwise return false
     */
	public boolean hasResponse(String request){
		if (!map.containsKey(request)){
			responseForCustomer = "Error 404: Page not found";
			return false;
		}else{
			return true;
		}
	}

    /**
     * Prints the entire hashmap
     */
	public void printMap(){
	    //????This might be wrong?
		System.out.println(map);
	}

    /**
     * Get the response based on the request
     * @param request   The request from proxy/client
     * @return          The response based on the request
     */
	public String getResponse(String request){
		if (this.hasResponse(request)){
			responseForCustomer = map.get(request);
		}
		return responseForCustomer;
	}


    /**
     * Send the Packet containing the response to proxy/client
     * @param message   The response message
     * @param recieverIP  The ip of proxy/client
     */
    public void sendingPacket(String message, InetAddress recieverIP) throws IOException{
        byte[] message_ = message.getBytes();
        //Message message = new Message(myIP, providerIP,request);
        DatagramPacket sendPacket = new DatagramPacket(message_, message_.length,recieverIP,9999);
        SOCKET.send(sendPacket);
    }
}
