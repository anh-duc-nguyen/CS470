package proxyNetworkTwo;

import java.net.SocketException;
import java.net.UnknownHostException;

public class Server extends NetworkEntity {
	
	public Server(String name) throws UnknownHostException, SocketException {
		super(name);
	}

}
