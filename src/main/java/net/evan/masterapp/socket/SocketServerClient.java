package net.evan.masterapp.socket;

import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.entities.ProxyEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class  SocketServerClient extends Thread {

    private final MasterAPP app;
    private SocketBalancer socketBalancer;

    private ProxyEntity mServer;
    private Socket clientSocket;
    private Socket serverSocket;
    private boolean areBothConnectionsAlive;
    private String clientHostPort;
    private String serverHostPort;

    public SocketServerClient(MasterAPP app, SocketBalancer socketBalancer, Socket clientSocket) {
        this.app = app;
        this.socketBalancer = socketBalancer;
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            clientHostPort = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
            serverSocket = createServerSocket();

            if (serverSocket == null) {  // If all the servers are down
                System.out.println("Can not establish connection for client " + clientHostPort + ". All the servers are down.");
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;

            }

            InputStream clientIn = clientSocket.getInputStream();
            OutputStream clientOut = clientSocket.getOutputStream();

            InputStream serverIn = serverSocket.getInputStream();
            OutputStream serverOut = serverSocket.getOutputStream();


            serverHostPort = InetAddress.getLocalHost().getHostAddress() + ":" + mServer.getPort();

            System.out.println("Forwarding  " + clientHostPort + " <--> " + serverHostPort + "  started.");

            SocketForwardThread clientForward = new SocketForwardThread(this, clientIn, serverOut);
            SocketForwardThread serverForward = new SocketForwardThread(this, serverIn, clientOut);

            areBothConnectionsAlive = true;

            clientForward.start();
            serverForward.start();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public synchronized void connectionBroken() {
        if (areBothConnectionsAlive) {
            try {
                serverSocket.close();
            } catch (IOException e) {}

            try {
                clientSocket.close();
            } catch (IOException e) {}

            areBothConnectionsAlive = false;

            mServer.getProxyDataEntity().setPlayers(mServer.getProxyDataEntity().getPlayers() - 1); //TODO: Update memory
            System.out.println("TCP Forwarding  " + clientHostPort + " <--> " + serverHostPort + "  stopped.");
        }

    }

    private Socket createServerSocket() {
        while (true) {
            mServer = getServerWithMinimalLoad();
            if (mServer == null)
                return null;
            try {
                Socket socket = new Socket(InetAddress.getLocalHost().getHostAddress(), mServer.getPort());
                mServer.getProxyDataEntity().setPlayers(mServer.getProxyDataEntity().getPlayers() + 1); //TODO: Update memory
                return socket;
            } catch (IOException ioe) {
                mServer.getProxyDataEntity().setAlive(false); //TODO: Update memory
            }
        }
    }

    private ProxyEntity getServerWithMinimalLoad() {
        String bestProxy = (String) app.getMasterProvider().getRedis().zrangeByScore("proxies", 0, 999999999).toArray()[0];
        ProxyEntity entity = app.getEntitiesManager().getProxyByName(bestProxy);
        app.getLogger().debug("[LBC] Best proxy is " + entity.getName() + " with port " + entity.getPort());
        return entity;
    }
}
