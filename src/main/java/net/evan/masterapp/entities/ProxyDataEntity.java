package net.evan.masterapp.entities;

import java.util.List;

public class ProxyDataEntity {

    private boolean isAlive;
    private int playerCount;

    public ProxyDataEntity() {
        this.isAlive = true;
        this.playerCount = 0;
    }

    public ProxyDataEntity(boolean isAlive, int playerCount) {
        this.isAlive = isAlive;
        this.playerCount = playerCount;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getPlayers() {
        return playerCount;
    }

    public void setPlayers(int playerCount) {
        this.playerCount = playerCount;
    }
}
