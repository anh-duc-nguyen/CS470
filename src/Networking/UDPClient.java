package Networking;

import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Random;

/**
 * The UDP Client
 * @author      Minghao Shan, ...........
 * @version     03/07/2018
 */
public class UDPClient implements Runnable
{
    DatagramSocket socket;
    private int msDelay;

    /**
     * Constructor
     */
    public UDPClient(){}

    /**
     * Executes the client
     */
    public void run()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the IP of the server: ");
        String serverIP = scan.nextLine();
        try
        {
            socket = new DatagramSocket();
            //set the time out to 10 seconds
            socket.setSoTimeout(10000);
            InetAddress IPAddress = InetAddress.getByName(serverIP);

            while (true)
            {
                byte[] incomingData = new byte[1024];
                msDelay = (new Random().nextInt(41) + 10) * 100;
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String request = getHeader(IPAddress.getHostAddress(), dateFormat.format(new Date())) +
                        "Request sent from client to connect\n" + getEnding();
                byte[] data = request.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 9876);
                socket.send(sendPacket);
                System.out.println("Message sent from client");
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                String response = new String(incomingPacket.getData(),"UTF-16");
                System.out.println("Response from server: " + response);
                Thread.sleep(msDelay);
            }
        }
        catch (IOException e)
        {
            socket.close();
            System.out.println("Client cannot connect to server");
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
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
        return "--------------------END--------------------\n";
    }

    /**
     * Main function
     */
    public static void main(String[] args)
    {
        UDPClient client = new UDPClient();
        client.run();
    }
}
