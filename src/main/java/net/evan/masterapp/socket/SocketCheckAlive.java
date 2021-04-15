package net.evan.masterapp.socket;

import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.entities.ProxyEntity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class SocketCheckAlive extends Thread {

    private final MasterAPP app;
    private SocketBalancer socketBalancer;

    public SocketCheckAlive(MasterAPP app, SocketBalancer socketBalancer) {
        this.app = app;
        this.socketBalancer = socketBalancer;
    }

    public void run() {
        while (!interrupted()) {
            try {
                Thread.sleep(socketBalancer.getCheckAliveIntervalMs());
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            try {
                checkAllDeadServers();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkAllDeadServers() throws UnknownHostException {
        List<ProxyEntity> serversL = app.getEntitiesManager().getProxyEntityList();
        for (int i = 0; i < serversL.size(); i++) {
            if (!serversL.get(i).getProxyDataEntity().isAlive()) {
                if (alive(InetAddress.getLocalHost().getHostAddress(), serversL.get(i).getPort())) {
                    app.getEntitiesManager().getProxyByName(serversL.get(i).getName()).getProxyDataEntity().setAlive(true); //TODO: Update memory
                }
            }
        }
    }

    private boolean alive(String host, int port) {
        boolean result = false;
        try {
            Socket s = new Socket(host, port);
            result = true;
            s.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return result;
    }

}
