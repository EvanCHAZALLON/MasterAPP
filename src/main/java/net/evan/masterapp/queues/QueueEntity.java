package net.evan.masterapp.queues;

import java.util.ArrayList;
import java.util.List;

public class QueueEntity {

    private String template;
    private List<QueuePlayer> playerList;

    public QueueEntity(String template) {
        this.template = template;
        this.playerList = new ArrayList<>();
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<QueuePlayer> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<QueuePlayer> playerList) {
        this.playerList = playerList;
    }
}
