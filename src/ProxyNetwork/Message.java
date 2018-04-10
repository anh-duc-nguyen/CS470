package ProxyNetwork;

import java.net.InetAddress;

public class Message {
	private byte[] messager = new byte[1024];
	private InetAddress senderIP, recieverIP;
	public Message(InetAddress from, InetAddress to, byte[] messager ){
		this.senderIP = from;
		this.recieverIP = to;
		this.messager = messager;
	}
	public InetAddress getSenderIP(){
		return this.senderIP;
	}
	public InetAddress getRecieverIP(){
		return this.recieverIP;
	}
	public byte[] getMessager(){
		return this.messager;
	}
	public int getLength(){
		return messager.length;
	}
	public String toString(){
		return "Messager from: "+this.getSenderIP()+  " to " + this.getRecieverIP() +": \n"
				+this.getMessager();
	}
	
}
