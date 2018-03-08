package Node;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class Node implements Runnable {
	DatagramSocket socket = null;
	ArrayList<InetAddress> connectedNode = new ArrayList();
	private int msDelay;
	static HashMap<InetAddress, Integer> upNodes = new HashMap<>();

	public Node() {
	}

	public static HashMap<InetAddress, Integer> getIPList() {
		return upNodes;
	}

	public void run() {

		Scanner scan = new Scanner(System.in);
		System.out.println("Enter ip: ");
		String myIP = scan.nextLine();
		System.out.println("enter port number: ");
		int p = scan.nextInt();

		try {
			if (!connectedNode.contains(myIP)) {
				connectedNode.add(InetAddress.getByName(myIP));
			}
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		try {
			System.out.println("Joining Socket: " + p);
			socket = new DatagramSocket(p);
			byte[] incomingData = new byte[1024];
			socket.setSoTimeout(30000);

			while (true) {

				DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
				try {
					socket.receive(incomingPacket);
					String message = new String(incomingPacket.getData());
					InetAddress IPAddress = incomingPacket.getAddress();
					if (!Arrays.asList(connectedNode).contains(IPAddress)) {
						connectedNode.add(IPAddress);
					}
					int port = incomingPacket.getPort();
					System.out.println("Received message from Node: " + message);
					System.out.println("Peer IP: " + IPAddress.getHostAddress());
					System.out.println("Peer port: " + port);
					System.out.printf("This Node is currently connec to: %d ", connectedNode.size());
					upNodes.put(IPAddress, 0);

					System.out.println("Peers: " + upNodes.toString() + "\n");

					String reply = upNodes.toString();
					byte[] data = reply.getBytes();
					for (InetAddress ip : connectedNode) {
						DatagramPacket replyPacket = new DatagramPacket(data, data.length, ip, port);
						socket.send(replyPacket);
					}
				} catch (SocketTimeoutException e) {
					System.out.println("Timeout... I'm the first one in the server");
					while (true) {
						msDelay = (new Random().nextInt(5) + 1) * 1000;
						String sentence = "This is a boo";
						byte[] data = sentence.getBytes();
						for (InetAddress ip : connectedNode) {
							DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip, p);
							System.out.println("Sending package to:" + ip);
							socket.send(sendPacket);
						}
						try {
							socket.receive(incomingPacket);
							String response = new String(incomingPacket.getData());
							System.out.println("Response from server: " + response);
							Thread.sleep(msDelay);
						} catch (SocketTimeoutException e2) {

						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		Node server = new Node();
		HashMap<InetAddress, Integer> upNodes = Node.getIPList();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				for (InetAddress key : upNodes.keySet()) {
					int curr = upNodes.get(key);
					curr++;
					if (curr >= 30) {
						System.out.println(key.toString() + " has timed out");
						upNodes.remove(key);
					} else {
						upNodes.put(key, curr);
					}
				}
			}
		}, 0, 1000);
		server.run();
	}
}
