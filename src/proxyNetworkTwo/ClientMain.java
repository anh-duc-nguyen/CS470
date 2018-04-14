package proxyNetworkTwo;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientMain {

	public static void main(String[] args) throws IOException {
		String name = "Anh Nguyen-PC";
		Client aClient = new Client(name);
		aClient.setRequest("Hello their");
		aClient.sendingPacket();
	}
}
