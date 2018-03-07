package Networking;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Timer;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class UDPServer implements Runnable
{
    DatagramSocket socket = null;
    HashMap<InetAddress, LocalDateTime> upNodes = new HashMap<>();

    public UDPServer()
    {
    }

    public HashMap<InetAddress, LocalDateTime> getIPList()
    {
        return upNodes;
    }

    public void run()
    {
        System.out.println("Listening...");

        try
        {
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
                    socket.setSoTimeout(30000);
                    System.out.println("Received message from client: " + message);
                    System.out.println("Client IP: " + IPAddress.getHostAddress());
                    System.out.println("Client port: " + port);

                    LocalDateTime currentTime = LocalDateTime.now();
                    upNodes.put(IPAddress, currentTime);

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
        server.run();
    }
}