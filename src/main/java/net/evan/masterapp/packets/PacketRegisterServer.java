package net.evan.masterapp.packets;

import net.evan.masterapp.MasterAPP;

public class PacketRegisterServer implements Packet {

    private final MasterAPP app;

    private String serverName;

    public PacketRegisterServer(MasterAPP app, String serverName) {
        this.app = app;
        this.serverName = serverName;
    }

    @Override
    public void invoke(String target) {
        app.getMasterProvider().getRabbitEventManager().sendMessage(target, channel(), serverName);
    }

    @Override
    public String channel() {
        return "registerServer";
    }
}
