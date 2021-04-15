package net.evan.masterapp.packets;

import net.evan.masterapp.MasterAPP;

public class PacketMovePlayer implements Packet {

    private final MasterAPP app;

    private String playerName;
    private String targetServer;

    public PacketMovePlayer(MasterAPP app, String playerName, String playerProxy, String targetServer) {
        this.app = app;
        this.playerName = playerName;
        this.targetServer = targetServer;
    }

    @Override
    public void invoke(String target) {
        app.getMasterProvider().getRabbitEventManager().sendMessage(target, channel(), playerName, targetServer);
    }

    @Override
    public String channel() {
        return "movePlayer";
    }
}
