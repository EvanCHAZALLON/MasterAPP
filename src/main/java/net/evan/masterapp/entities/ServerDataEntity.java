package net.evan.masterapp.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ServerDataEntity {

    private String template;
    private List<String> players;

    private int minSlots;
    private int maxSlots;

    private long startedAt;

    private boolean isStatic;
    private Status status;

    private String host;

    public ServerDataEntity(String template, int minSlots, int maxSlots, boolean isStatic) {
        this.template = template;
        this.players = new ArrayList<>();
        this.minSlots = minSlots;
        this.maxSlots = maxSlots;
        this.startedAt = System.currentTimeMillis();
        this.isStatic = isStatic;
        this.status = Status.STARTING;
    }

    public ServerDataEntity(String template, int minSlots, int maxSlots, boolean isStatic, String host) {
        this.template = template;
        this.players = new ArrayList<>();
        this.minSlots = minSlots;
        this.maxSlots = maxSlots;
        this.startedAt = System.currentTimeMillis();
        this.isStatic = isStatic;
        this.status = Status.STARTING;
        this.host = host;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        System.out.println("Players set to " + players.size());
        this.players = players;
    }

    public int getMinSlots() {
        return minSlots;
    }

    public void setMinSlots(int minSlots) {
        this.minSlots = minSlots;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public void setMaxSlots(int maxSlots) {
        this.maxSlots = maxSlots;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public enum Status {
        STARTING, WAITING_FOR_PLAYERS, IN_GAME, FINISHED, REBOOTING, NOT_RESPONDING;
    }

}
