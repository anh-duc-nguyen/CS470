package proxyNetwork;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This is the base class where Client, Proxy and Server inherent from.
 * @author  Minghao Shan, Anh Nguyen, Andrew Rodeghero, Harshavardhan Reddy Madduri
 * @version 04/22/2018
 */
public class NetworkEntity {
	private String name;
	protected InetAddress myIP;
	protected final DatagramSocket SOCKET;
	public NetworkEntity(String name) throws UnknownHostException, SocketException{
		this.SOCKET = new DatagramSocket(9999);
		this.name = name;
		this.myIP = InetAddress.getLocalHost();
	}

    /**
     * @return name of the entity
     */
	public String getName(){
		return this.name;
	}

    /**
     * @return ip address of the entity
     */
	public InetAddress getIP(){
		return this.myIP;
	}

    /**
     * @return datagram socket of that entity
     */
	public DatagramSocket getSocket(){
		return this.SOCKET;
	}

    /**
     * @return string format of the entity, including name and ip address
     */
	public String toString(){
		return this.getName() + ": "+this.getIP();
	}
}
