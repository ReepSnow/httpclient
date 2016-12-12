package cn.snow.httpclient;

import org.apache.http.conn.HttpClientConnectionManager;

public class IdleConnectionEvictor extends Thread {

    private final HttpClientConnectionManager connMgr;

    private volatile boolean shutdown;
    //waitTime该变量用来控制连接的时间，防止连接不响应时导致连接长时间存在浪费资源
    private int waitTime; 
    public IdleConnectionEvictor(HttpClientConnectionManager connMgr,int waitTime) {
        this.connMgr = connMgr;
        this.waitTime = waitTime;
        // 启动当前线程
        this.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(waitTime);
                    // 关闭失效的连接
                    connMgr.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }
//一个连接请求结束后，会调用该方法，唤醒run方法里面的wait()方法关闭连接，如果连接一直不结束，wait方法会等waitTime毫秒，然后直接关闭该链接
    public void shutdown() {
        shutdown = true;
        System.out.println("清理连接");
        synchronized (this) {
            notifyAll();
        }
    }
}