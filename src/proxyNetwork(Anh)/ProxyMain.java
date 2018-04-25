package proxyNetwork;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * The main program for proxy to run
 * @author  Minghao Shan, Anh Nguyen, Andrew Rodeghero, Harshavardhan Reddy Madduri
 * @version 04/22/2018
 */
public class ProxyMain {

	public static void main(String[] args) throws IOException {
		Proxy myProxy = new Proxy("Anh-PC");
		while (true){
			myProxy.runThreads();

			DatagramPacket clientPacket = myProxy.recievingPacket();
			if (! myProxy.isNewClient(clientPacket.getAddress()))
				myProxy.connectedNewClient(clientPacket.getAddress(), new String(clientPacket.getData(),"US-ASCII"));
		}
	}

}
