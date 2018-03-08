package Networking;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * The UDP Client
 * @author      Minghao Shan, ...........
 * @version     03/07/2018
 */
public class UDPServer implements Runnable
{
    private DatagramSocket socket = null;
    private static HashMap<InetAddress, Integer> upNodes = new HashMap<>();

    /**
     * Constructor
     */
    private UDPServer(){}

    private static HashMap<InetAddress, Integer> getIPList()
    {
        return upNodes;
    }

    /**
     * Executes the server
     */
    public void run()
    {
        System.out.println("Listening...");

        try {
            socket = new DatagramSocket(9876);
            byte[] incomingData = new byte[1024];

            while (true)
            {
                //receive the packet from client
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                String message = new String(incomingPacket.getData());
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                System.out.println("Received message from client: " + message);
                System.out.println("Client IP: " + IPAddress.getHostAddress());
                System.out.println("Client port: " + port);
                upNodes.put(IPAddress, 0);
                System.out.println("Clients: " + upNodes.toString() + "\n");
                String reply = upNodes.toString();
                byte[] data = reply.getBytes();
                DatagramPacket replyPacket = new DatagramPacket(data, data.length, IPAddress, port);
                socket.send(replyPacket);
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Removes disconnected client.
     */
    public static void removeClient() {
        HashMap<InetAddress, Integer> upNodes = UDPServer.getIPList();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Iterator<HashMap.Entry<InetAddress, Integer>> iterator = upNodes.entrySet().iterator();
                while (iterator.hasNext())
                {
                    HashMap.Entry entry = (HashMap.Entry)iterator.next();
                    InetAddress key = (InetAddress) entry.getKey();
                    int value = (Integer) entry.getValue();
                    value++;
                    if (value >= 30)
                    {
                        System.out.println(key.toString() + " has timed out" + "\n");
                        iterator.remove();
                    }
                    else
                    {
                        upNodes.put(key, value);
                    }
                }
            }
        }, 0, 1000);
    }
    public static void main(String[] args)
    {

        UDPServer server = new UDPServer();
        server.run();
        removeClient();
    }
}
