package proxyNetworkTwo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ProxyMain {

	public static void main(String[] args) throws IOException {
		Proxy myProxy = new Proxy("Anh-PC");
		while (true){
			myProxy.runn();

			DatagramPacket clientPacket = myProxy.recievingPacket();
			if (! myProxy.newCustomer(clientPacket.getAddress()))
				myProxy.newClientConnected(clientPacket.getAddress(), new String(clientPacket.getData(),"US-ASCII"));
		}
	}

}
