package ProxyNetwork;
import java.lang.Thread;
import java.net.InetAddress;
public class ProxyThread extends Thread {
	private static String name;
	private static InetAddress myIPAddress;
	private InetAddress cusIPAddress;
	private InetAddress serIPAddress;
	public ProxyThread(String name, InetAddress myIPAddress, InetAddress cusIPAddress){
		this.name = name;
		this.myIPAddress = myIPAddress;
		this.cusIPAddress = cusIPAddress;
	}
	public void setServerIP(InetAddress serIPAddress){
		this.serIPAddress = serIPAddress;
	}
	public void run(){
		
	}
}
