package ProxyNetwork;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientTest {

	private static Scanner scan;

	public static void main(String[] args) throws IOException {
		scan = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scan.nextLine();
        System.out.println("Enter your name: ");
        String proxyName = scan.nextLine();
        Client aClient = new Client(name);
        InetAddress providerIP = InetAddress.getByName("150.243.17.82");
        aClient.setProvider(providerIP);
        //aClient.closeSocket();
        System.out.println("Hello, you are:");
        System.out.println(aClient.toString());
        String request = "";
        while(!request.equals("quit")){
	        System.out.println("Enter your request: ");
	        request = scan.nextLine();
	        byte[] request_ = request.getBytes();
	        aClient.setRequest(request_);
	        aClient.sendingPacket();
		}
        aClient.closeSocket();
	}
}
