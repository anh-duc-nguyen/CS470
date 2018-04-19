package proxyNetworkTwo;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
public class ClientMain {
	static Scanner in = new Scanner(System.in);
	public static void main(String[] args) throws IOException {
		String name = "Anh Nguyen-PC";
		Client aClient = new Client(name);
		
		aClient.setProvider("150.243.17.14");
		String Query = "";
		
		while (!Query.equals("exit")){
			System.out.println("Please enter Query:(exit to stop) ");
			Query = in.nextLine();
			aClient.setRequest(Query);
			aClient.sendingPacket();
		}
	}
}
