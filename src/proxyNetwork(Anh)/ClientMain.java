package proxyNetwork;

import java.io.IOException;
import java.util.Scanner;

/**
 * This is the main class where the client will start running.
 * @author  Minghao Shan, Anh Nguyen, Andrew Rodeghero, Harshavardhan Reddy Madduri
 * @version 04/22/2018
 */
public class ClientMain {
	static Scanner in = new Scanner(System.in);
	public static void main(String[] args) throws IOException {
		String name = "Anh Nguyen-PC";
		Client aClient = new Client(name);

		//connect to the proxy/server
		aClient.setProvider("150.243.16.1");
		String Query = "";
		
		while (!Query.equals("exit")){
			System.out.println("Please enter Query:(exit to stop) ");
			Query = in.nextLine();
			aClient.setRequest(Query);
			aClient.sendingPacket();
			aClient.recievingPacket();
		}
	}
}


//get:foo.com