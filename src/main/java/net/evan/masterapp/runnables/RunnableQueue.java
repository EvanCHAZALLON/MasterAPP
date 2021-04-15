package net.evan.masterapp.runnables;

import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.entities.ServerDataEntity;
import net.evan.masterapp.entities.ServerEntity;
import net.evan.masterapp.packets.PacketMovePlayer;
import net.evan.masterapp.queues.QueueEntity;
import net.evan.masterapp.queues.QueuePlayer;

import java.util.Map;
import java.util.Objects;

public class RunnableQueue implements Runnable {

    private final MasterAPP app;

    public RunnableQueue(MasterAPP app) {
        this.app = app;
    }

    @Override
    public void run() {
        for (QueueEntity queue : app.getQueueManager().getQueueEntityList()) {

            for (QueuePlayer player : queue.getPlayerList()) {

                if (!isServerAvailable(queue.getTemplate())) {

                    if (!isAnyServerStarting(queue.getTemplate())) {

                        ServerEntity en = app.getEntitiesManager().launch(new ServerEntity(queue.getTemplate() + "Test", new ServerDataEntity(queue.getTemplate(), 0, 10, false)));
                        app.getQueueManager().getStartingServerMap().put(queue.getTemplate(), en);
                        app.getQueueManager().getWaitingFor().put(player, en);

                    } else {

                        ServerEntity en = getServerStarting(queue.getTemplate());
                        app.getQueueManager().getWaitingFor().put(player, en);

                    }

                } else {

                    this.movePlayerTo(player.getPlayerName(), Objects.requireNonNull(findServer(queue.getTemplate())).getName());

                }

                //TODO: this.removeFromQueue(queue.getTemplate(), player);
                //TODO: Remove player from queue (Without block the main thread)

            }

        }
    }

    private boolean isServerAvailable(String template) {
        for (ServerEntity serverEntity : app.getEntitiesManager().getServerEntityList()) {
            if (serverEntity.getData().getTemplate().equalsIgnoreCase(template) && serverEntity.getData().getStatus() == ServerDataEntity.Status.WAITING_FOR_PLAYERS) {
                return true;
            }
        }
        return false;
    }

    private boolean isAnyServerStarting(String template) {
        for (Map.Entry entry : app.getQueueManager().getStartingServerMap().entrySet()) {
            String t = (String) entry.getKey();
            return t.equalsIgnoreCase(template);
        }
        return false;
    }

    private ServerEntity getServerStarting(String template) {
        for (Map.Entry entry : app.getQueueManager().getStartingServerMap().entrySet()) {
            String t = (String) entry.getKey();
            ServerEntity en = (ServerEntity) entry.getValue();
            if (t.equalsIgnoreCase(template)) {
                return en;
            }
            return null;
        }
        return null;
    }

    private void movePlayerTo(String playerName, String serverName) {
        app.getLogger().debug("Moving player " + playerName + " to " + serverName);
        new PacketMovePlayer(app, playerName, "proxyTest", serverName).invoke("proxyTest");
    }

    private ServerEntity findServer(String template) {
        for (ServerEntity entity : app.getEntitiesManager().getServerEntityList()) {
            if (entity.getData().getTemplate().equalsIgnoreCase(template)) {
                if (entity.getData().getStatus() == ServerDataEntity.Status.WAITING_FOR_PLAYERS) {
                    return entity;
                }
            }
        }
        return null;
    }

    private void removeFromQueue(String template, QueuePlayer queuePlayer) {
        app.getQueueManager().removePlayerFromQueue(template, queuePlayer.getPlayerName());
    }

}
