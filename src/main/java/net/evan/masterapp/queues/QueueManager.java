package net.evan.masterapp.queues;

import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.entities.ServerEntity;

import java.util.*;

public class QueueManager {

    private final MasterAPP app;
    private List<QueueEntity> queueEntityList;

    private Map<String, ServerEntity> startingServerMap;
    private Map<QueuePlayer, ServerEntity> waitingFor;

    public QueueManager(MasterAPP app) {
        this.app = app;
        this.queueEntityList = new ArrayList<>();
        this.waitingFor = new HashMap<>();
        this.startingServerMap = new HashMap<>();
        app.getLogger().info("QueueManager was succefully loaded.");
    }

    public void addQueue(QueueEntity queueEntity) {
        queueEntityList.add(queueEntity);
        app.getLogger().info("Created " + queueEntity.getTemplate() + " queue.");
    }

    public void removeQueue(String queueTemplate) {
        queueEntityList.stream().filter(q -> q.getTemplate().equals(queueTemplate)).findAny().ifPresent(g -> queueEntityList.remove(g));
    }

    public QueueEntity getQueue(String queueTemplate) {
        Optional<QueueEntity> oQ = queueEntityList.stream().filter(q -> q.getTemplate().equals(queueTemplate)).findAny();
        return oQ.orElse(null);
    }

    public boolean exists(String queueTemplate) {
        return getQueue(queueTemplate) != null;
    }

    /*
    Player part
     */

    public void addPlayerToQueue(String queueTemplate, QueuePlayer queuePlayer) {
        if (!exists(queueTemplate)) return;
        QueueEntity queueEntity = getQueue(queueTemplate);
        List<QueuePlayer> queuePlayers = queueEntity.getPlayerList(); //Getting current list
        queuePlayers.add(queuePlayer); //Adding the asked player

        queueEntity.setPlayerList(queuePlayers); //Refreshing the list
    }

    public void removePlayerFromQueue(String template, String playerName) {
        if (exists(template)) {
            app.getLogger().debug("Exists template " + template);
            List<QueuePlayer> actualPlayers = getQueue(template).getPlayerList();
            app.getLogger().debug("List: " + actualPlayers);

            for (QueuePlayer player : actualPlayers) {
                if (player.getPlayerName().equalsIgnoreCase(playerName)) {
                    actualPlayers.remove(player);
                    app.getLogger().debug("Player " + playerName + " was succefully removed from queue " + template);
                    getQueue(template).setPlayerList(actualPlayers);
                    app.getLogger().debug("New list set!");
                }
            }

        }

    }

    public boolean isPlayerInQueue(String queueTemplate, String queuePlayerName) {
        if (!exists(queueTemplate)) return false;
        QueueEntity queueEntity = getQueue(queueTemplate);
        return queueEntity.getPlayerList().stream().anyMatch(p -> p.getPlayerName().equals(queuePlayerName)); //Checking if the list contains a QueuePlayer which name equals to queuePlayerName
    }

    public List<QueueEntity> getQueueEntityList() {
        return queueEntityList;
    }

    public Map<String, ServerEntity> getStartingServerMap() {
        return startingServerMap;
    }

    public Map<QueuePlayer, ServerEntity> getWaitingFor() {
        return waitingFor;
    }
}
