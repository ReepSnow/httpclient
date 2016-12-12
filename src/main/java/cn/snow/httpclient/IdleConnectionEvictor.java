package cn.snow.httpclient;

import org.apache.http.conn.HttpClientConnectionManager;

public class IdleConnectionEvictor extends Thread {

    private final HttpClientConnectionManager connMgr;

    private volatile boolean shutdown;
    private int waitTime; 
    public IdleConnectionEvictor(HttpClientConnectionManager connMgr,int waitTime) {
        this.connMgr = connMgr;
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

    public void shutdown() {
        shutdown = true;
        System.out.println("清理连接");
        synchronized (this) {
            notifyAll();
        }
    }
}