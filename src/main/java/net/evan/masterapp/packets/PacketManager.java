package net.evan.masterapp.packets;

import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.utils.rabbit.RabbitEventManager;

public class PacketManager {

    private final MasterAPP app;

    public PacketManager(MasterAPP app) {
        this.app = app;
        this.register();
        app.getLogger().info("PacketManager was succefully loaded.");
    }

    private void register() {
        RabbitEventManager manager = app.getMasterProvider().getRabbitEventManager();

        //manager.registerListener(new MyClass());
    }



}
