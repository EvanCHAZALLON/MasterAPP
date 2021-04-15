package net.evan.masterapp.queues;

public class QueuePlayer {

    private String playerName;
    private String map;

    public QueuePlayer(String playerName, String map) {
        this.playerName = playerName;
        this.map = map;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
