package Networking;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import java.text.SimpleDateFormat;

public class UDPServer implements Runnable
{
    DatagramSocket socket = null;
    static HashMap<InetAddress, Integer> upNodes = new HashMap<>();



    public UDPServer()
    {
    }

    public static HashMap<InetAddress, Integer> getIPList()
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

                    DatagramPacket replyPacket = new DatagramPacket(data, data.length, IPAddress, port);
                    socket.send(replyPacket);
                }
                catch (SocketTimeoutException e)
                {
                    System.out.println("Timeout...");
                    //upNodes.put(IPAddress, true);
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