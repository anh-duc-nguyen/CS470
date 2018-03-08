package Node;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class Node implements Runnable
{
    DatagramSocket socket = null;
    ArrayList<InetAddress> connectedNode = new ArrayList();
    private int msDelay;
    static HashMap<InetAddress, Integer> upNodes = new HashMap<>();

    public Node()
    {
    }

    public static HashMap<InetAddress, Integer> getIPList()
    {
        return upNodes;
    }

    public void run()
    {

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter ip: ");
        String myIP = scan.nextLine();
        System.out.println("enter port number: ");
        int p = scan.nextInt();
        //String curent_ip = scan.nextLine();
        try {
            connectedNode.add(InetAddress.getByName(myIP));
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        /**
         Scanner scan = null;
         try {
         scan = new Scanner(new File("IP.txt"));
         } catch (FileNotFoundException e1) {
         e1.printStackTrace();
         }
         System.out.println("Importing data from txt");
         String serverIP= scan.nextLine();
         */
        //"150.243.64.254"


        try {
            System.out.println("Joining Socket: " + p);
            socket = new DatagramSocket(p);
            byte[] incomingData = new byte[1024];
            socket.setSoTimeout(30000);
            //socket.close();
            while (true)
            {

                DatagramPacket incomingPacket = new DatagramPacket(incomingData,
                        incomingData.length);
                try {
                    socket.receive(incomingPacket);
                    String message = new String(incomingPacket.getData());
                    InetAddress IPAddress = incomingPacket.getAddress();
                    if(!Arrays.asList(connectedNode).contains(IPAddress)){
                        connectedNode.add(IPAddress);
                    }
                    int port = incomingPacket.getPort();
                    System.out.println("Received message from Node: " + message);
                    System.out.println("Client IP: " + IPAddress.getHostAddress());
                    System.out.println("Client port: " + port);
                    System.out.printf("This Node is currently connecto: %d ",connectedNode.size());
                    upNodes.put(IPAddress, 0);

                    System.out.println("Clients: " + upNodes.toString() + "\n");

                    String reply = upNodes.toString();
                    byte[] data = reply.getBytes();
                    for (InetAddress ip : connectedNode){
                        DatagramPacket replyPacket = new DatagramPacket(data, data.length, ip, port );
                        socket.send(replyPacket);
                    }
                }
                catch (SocketTimeoutException e)
                {
                    System.out.println("Timeout... I'm the first one in the server");
                    while (true)
                    {
                        msDelay = (new Random().nextInt(41) + 10) * 100;
                        String sentence = "This is a boo";
                        byte[] data = sentence.getBytes();
                        for (InetAddress ip: connectedNode){
                            DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip, p);
                            System.out.println("Sending package to:" + ip);
                            socket.send(sendPacket);
                        }
                        try {
                            socket.receive(incomingPacket);
                            //incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                            //socket.receive(inPackage);
                            String response = new String(incomingPacket.getData());
                            System.out.println("Response from server: " + response);
                        } catch(SocketTimeoutException e2){
                            //We good !
                        }
                    }
                }
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {

        Node server = new Node();
        HashMap<InetAddress, Integer> upNodes = Node.getIPList();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                for (InetAddress key : upNodes.keySet()){
                    int curr = upNodes.get(key);
                    curr++;
                    if (curr >= 30){
                        System.out.println(key.toString() + " has timed out");
                        upNodes.remove(key);
                    }else {
                        upNodes.put(key, curr);
                    }
                }
            }
        }, 0, 1000);
        server.run();
    }
}