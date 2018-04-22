package proxyNetworkTwo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerMain {

	public static void main(String[] args) throws IOException {
		Server myServer = new Server("foo");
		DatagramPacket request;
		String respond;

		myServer.newEntry(myServer.getName(), "Online");
		myServer.newEntry("get:foo.com", "foo.html");
		myServer.newEntry("get:foo.com/login", "foo/login.html");
		myServer.newEntry("get:foo.com/about", "foo/about.html");
		myServer.newEntry("get:foo.com/home", "foo/home.html");
		myServer.newEntry("get:foo.com/contact", "foo/contact.html");
		
		while(true){
			request = myServer.recievingPacket();
			String request_ = new String(request.getData(),"US-ASCII");
			respond = myServer.getRespond(request_.trim());
			myServer.sendingPacket(respond, request.getAddress());
			System.out.print("sending packet to");
			System.out.print(request.getAddress().getHostAddress());
		}
	}
}