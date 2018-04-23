package proxyNetwork;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Main program for Server to run.
 * @author  Minghao Shan, Anh Nguyen, Andrew Rodeghero, Harshavardhan Reddy Madduri
 * @version 04/22/2018
 */
public class ServerMain {

	public static void main(String[] args) throws IOException {
		Server myServer = new Server("foo");
		DatagramPacket request;
		String respond;

		myServer.createEntry(myServer.getName(), "Online");
		myServer.createEntry("get:foo.com", "foo.html");
		myServer.createEntry("get:foo.com/login", "foo/login.html");
		myServer.createEntry("get:foo.com/about", "foo/about.html");
		myServer.createEntry("get:foo.com/home", "foo/home.html");
		myServer.createEntry("get:foo.com/contact", "foo/contact.html");
		
		while(true){
			request = myServer.recievingPacket();
			String request_ = new String(request.getData(),"US-ASCII");
			respond = myServer.getResponse(request_.trim());
			myServer.sendingPacket(respond, request.getAddress());
			System.out.print("sending packet to");
			System.out.print(request.getAddress().getHostAddress());
		}
	}
}