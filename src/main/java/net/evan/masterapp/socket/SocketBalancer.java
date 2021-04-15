package net.evan.masterapp.socket;

import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.logger.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketBalancer {

    private final MasterAPP app;

    private final int listeningPort;
    private Logger logger;
    private boolean useLoadBalancingAlgorithm = true;
    private long checkAliveIntervalMs = 5 * 1000;

    public SocketBalancer(MasterAPP app, int listeningPort) {
        this.app = app;
        this.listeningPort = listeningPort;
        this.logger = app.getLogger();
        try {
            this.readSettings();
            this.startCheckAliveThread();
            this.startForwardServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getCheckAliveIntervalMs() {
        return checkAliveIntervalMs;
    }

    public boolean isLoadBalancingEnabled() {
        return useLoadBalancingAlgorithm;
    }

    public void readSettings() {
        useLoadBalancingAlgorithm = true;
        checkAliveIntervalMs = 5 * 1000;
    }

    private void startCheckAliveThread() {
        SocketCheckAlive socketCheckAlive = new SocketCheckAlive(app, this);
        socketCheckAlive.setDaemon(true);
        socketCheckAlive.start();
    }

    public void startForwardServer() throws Exception {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(listeningPort);
        } catch (IOException ioe) {
            throw new IOException("Unable to bind to port " + listeningPort);
        }
        logger.info("Master Load Balancer Proxy has started on TCP port " + listeningPort + ".");
        logger.info("All TCP connections to " + InetAddress.getLocalHost().getHostAddress() + ":" + listeningPort + " will be forwarded to the following servers:");

        logger.debug("Load balancing algorithm is " + (useLoadBalancingAlgorithm ? "ENABLED." : "DISABLED."));

        this.app.getExecutorService().execute(() -> {
            /*try {
                System.out.println("Runned LBC Task");

                Socket clientSocket = serverSocket.accept();
                String clientHostPort = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();

                System.out.println("ClientSocket=" + clientSocket);
                System.out.println("ClientHostPort=" + clientHostPort);

                logger.debug("Accepted client from " + clientHostPort);

                openSocketServerCli(clientSocket);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        });
    }

    private void openSocketServerCli(Socket clientSocket) {
        SocketServerClient forwardThread = new SocketServerClient(app, this, clientSocket);
        forwardThread.start();
    }

}
