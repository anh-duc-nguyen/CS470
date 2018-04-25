package Proxy;

import java.util.concurrent.ConcurrentHashMap;

public class ProxyThread {
    public ConcurrentHashMap<String, String> cache;
    public Proxy proxy;
    public ProxyThread(ConcurrentHashMap<String, String> cache) {
        this.cache = cache;
        proxy = new Proxy();

    }

    public void run () {
        proxy.receive
    }
}
