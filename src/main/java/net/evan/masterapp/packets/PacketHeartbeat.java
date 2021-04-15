package net.evan.masterapp.packets;

import net.evan.masterapp.MasterAPP;

public class PacketHeartbeat implements Packet {

    private final MasterAPP app;

    public PacketHeartbeat(MasterAPP app) {
        this.app = app;
    }

    @Override
    public void invoke(String target) {
        app.getMasterProvider().getRabbitEventManager().sendMessage(target, channel());
    }

    @Override
    public String channel() {
        return "heartbeat";
    }

}
