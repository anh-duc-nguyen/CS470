package Proxy;

import java.util.concurrent.ConcurrentHashMap;

public class ProxyMain {
    public static ConcurrentHashMap<String, String> cache;

    public static void main(String[] args) {
        ProxyThread a = new ProxyThread(cache);
        a.run();
    }
}
