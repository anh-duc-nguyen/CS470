package Networking;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class UDPServer implements Runnable
{
    private DatagramSocket socket = null;
    private static HashMap<InetAddress, Integer> upNodes = new HashMap<>();



    private UDPServer()
    {
    }

    private static HashMap<InetAddress, Integer> getIPList()
    {
        return upNodes;
    }

    public void run()
    {
        System.out.println("Listening...");

        try {
            socket = new DatagramSocket(9876);
            byte[] incomingData = new byte[1024];

            while (true)
            {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData,
                        incomingData.length);
                try {
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
                    /**
                     * Debug
                     */
                    System.out.println(new String(data) + "\n\n");

                    DatagramPacket replyPacket = new DatagramPacket(data, data.length, IPAddress, port);
                    socket.send(replyPacket);
                }
                catch (SocketTimeoutException e)
                {
                    System.out.println("SocketTimeoutException");
                    socket.close();
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

        UDPServer server = new UDPServer();
        HashMap<InetAddress, Integer> upNodes = UDPServer.getIPList();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Iterator<HashMap.Entry<InetAddress, Integer>> iterator = upNodes.entrySet().iterator();
                while (iterator.hasNext())
                {
                    HashMap.Entry entry = (HashMap.Entry)iterator.next();
                    InetAddress key = (InetAddress) entry.getKey();
                    int val = (Integer) entry.getValue();
                    val++;
                    if (val >= 30)
                    {
                        System.out.println(key.toString() + " has timed out" + "\n");
                        iterator.remove();
                    }
                    else
                    {
                        upNodes.put(key, val);
                    }
                }
            }
        }, 0, 1000);
        server.run();
    }
}