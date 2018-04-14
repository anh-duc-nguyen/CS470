package ProxyNetwork;

import java.io.IOException;
import java.util.Scanner;

public class ProxyTest {

	private static Scanner scan;

	public static void main(String[] args) throws IOException {
		scan = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scan.nextLine();
        Proxy aProxy = new Proxy(name);
        System.out.println("Hello, you are:");
        System.out.println(aProxy.toString()); 
	}
}
