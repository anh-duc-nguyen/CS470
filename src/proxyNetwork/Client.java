package proxyNetwork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This class inherent from Network Entity class and
 * @author  Minghao Shan, Anh Nguyen, Andrew Rodeghero, Harshavardhan Reddy Madduri
 * @version 04/22/2018
 */
public class Client extends NetworkEntity {
	private InetAddress providerIP;
	private String request;
	private byte[] message;

    /**
     * Constructor
     * @param name      Name of the client.
     */
	public Client(String name) throws UnknownHostException, SocketException {
		super(name);
	}

    /**
     * @param provider  The ip address of proxy/server.
     */
	public void setProvider(String provider) throws UnknownHostException{
		this.providerIP = InetAddress.getByName(provider);
	}

    /**
     * @param request   The request to send to the proxy/server.
     */
	public void setRequest(String request){
		this.request = request;
	}

    /**
     * @return          The request that client sent.
     */
	public String getRequest(){
		return this.request;
	}

    /**
     * Sends a packet that contains the request
     */
	public void sendingPacket() throws IOException{


		//Message message = new Message(myIP, providerIP,request);

        this.message = request.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(message, message.length,providerIP,9999);
        SOCKET.send(sendPacket);
	}

    /**
     * Receives the packet from the proxy/server and print out the respond.
     * @return          The packet received from the proxy/server.
     */
	public DatagramPacket recievingPacket() throws IOException{
		byte[] inMessage = new byte[1024];
		DatagramPacket inPacket = new DatagramPacket(inMessage, inMessage.length);
		SOCKET.receive(inPacket);
		System.out.println(new String(inPacket.getData(),"US-ASCII"));
		return inPacket;
	}
}
