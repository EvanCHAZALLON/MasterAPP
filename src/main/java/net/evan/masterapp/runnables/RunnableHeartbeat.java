package net.evan.masterapp.runnables;

import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.entities.ServerDataEntity;
import net.evan.masterapp.packets.PacketHeartbeat;

public class RunnableHeartbeat implements Runnable {

    private final MasterAPP app;

    public RunnableHeartbeat(MasterAPP app) {
        this.app = app;
    }

    @Override
    public void run() {
        app.getEntitiesManager().getServerEntityList().forEach(s -> {
            if (s.getData().getStatus() != ServerDataEntity.Status.STARTING && s.getData().getStatus() != ServerDataEntity.Status.REBOOTING && s.getData().getStatus() != ServerDataEntity.Status.NOT_RESPONDING) {
                new PacketHeartbeat(app).invoke(s.getName());
            }
        });
    }
}
