package Networking;

import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The UDP Server
 * @author      Minghao Shan, Andrew Rodeghero, Anh Nguyen, Harshavardhan Madduri
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
                String message = new String(incomingPacket.getData(),"UTF-16");
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                System.out.println("Received message from client: " + message);
                System.out.println("Client IP: " + IPAddress.getHostAddress());
                System.out.println("Client port: " + port);
                upNodes.put(IPAddress, 0);
                System.out.println("Clients: " + upNodes.toString() + "\n");
                //send reply
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String reply = getHeader(IPAddress.getHostAddress(), dateFormat.format(new Date())) +
                        "List of available clients: \n" + upNodes.toString() + getEnding();
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

    /**
     * Get the header of the file
     */
    public static String getHeader(String receiverIP, String time) {
        return  "\n-------------------HEADER-------------------" + "\nSent to IP  :" + receiverIP +
                "\nTime stamp  : " + time + "\n--------------------------------------------\n";
    }

    /**
     * Get ending of the file
     */
    public static String getEnding() {
        return "\n--------------------END--------------------\n";
    }

    /**
     * Main Program
     */
    public static void main(String[] args)
    {
        UDPServer server = new UDPServer();
        removeClient();
        server.run();
    }
}
