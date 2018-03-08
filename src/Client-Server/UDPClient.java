package Networking;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.Random;

public class UDPClient implements Runnable {
	DatagramSocket socket;
	private int msDelay;

	public UDPClient() {

	}

	public void run() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the IP of the server: ");
		String serverIP = scan.nextLine();
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(10000);
			// Acquires IP Address of the server
			InetAddress IPAddress = InetAddress.getByName(serverIP);
			int packetNum = 1;
			int uptime = 0;

			while (true) {
				byte[] incomingData = new byte[1024];
				// Creating a delay
				msDelay = (new Random().nextInt(41) + 10) * 100;
				uptime += msDelay;
				String sentence = Integer.toString(packetNum);
				byte[] data = sentence.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 9876);
				socket.send(sendPacket);
				// Prints message after the packet is sent
				System.out.println("Message sent from client");
				packetNum++;

				try {
					DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
					socket.receive(incomingPacket);
					String response = new String(incomingPacket.getData());
					System.out.println("Response from server: " + response);

					System.out.println("Client uptime: " + uptime + "ms\n");

					Thread.sleep(msDelay);
				} catch (SocketTimeoutException e) {
					// Prints message when the client connection to server is lost & closes open socket
					System.out.println("Connection to server has been lost.");
					socket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		UDPClient client = new UDPClient();
		client.run();
	}
}