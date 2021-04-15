package net.evan.masterapp.runnables;

import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.entities.ServerDataEntity;
import net.evan.masterapp.entities.ServerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class RunnableHub implements Runnable {

    private final MasterAPP app;
    private final int MAX_SLOTS_HUB = 2;

    public RunnableHub(MasterAPP app) {
        this.app = app;
    }

    @Override
    public void run() {
        int numberOfHubs = app.getEntitiesManager().getByType("hub").size();
        AtomicInteger playersInHub = new AtomicInteger(0);
        app.getEntitiesManager().getByType("hub").forEach(srv -> {
            playersInHub.getAndAdd(srv.getData().getPlayers().size());
        });

        app.getEntitiesManager().getByType("auth").forEach(srv -> {
            playersInHub.getAndAdd(srv.getData().getPlayers().size());
        });

        int neededHubs = (int) Math.round((playersInHub.intValue() * 1.6) / MAX_SLOTS_HUB);
        if (neededHubs == 0) neededHubs = 1;

        app.getLogger().debug("Players in hub : " + playersInHub);
        app.getLogger().debug("Number of hubs: " + numberOfHubs);
        app.getLogger().debug("Needed hubs: " + neededHubs);

        if (numberOfHubs > neededHubs) {
            int difference = numberOfHubs - neededHubs;
            app.getLogger().debug("There's " + difference + " more hubs.");

            getLessServers(difference).forEach(srv -> {
                srv.getData().setStatus(ServerDataEntity.Status.FINISHED);
                app.getLogger().debug("Shutting down " + srv.getName());
                app.getEntitiesManager().shutdown(srv);
            });
        } else if (numberOfHubs < neededHubs) {
            int difference = neededHubs - numberOfHubs;
            app.getLogger().debug("There are " + difference + " hubs missing.");

            Random random = new Random();
            for (int i = 0; i < difference; i++) {
                app.getEntitiesManager().launch(new ServerEntity("newHub-0" + random.nextInt(50), new ServerDataEntity("hub", 0, 10, false)));
            }
        }

    }

    private List<ServerEntity> getLessServers(int range) {
        AtomicReference<ServerEntity> aR = new AtomicReference<>();
        List<ServerEntity> temporary = app.getEntitiesManager().getByType("hub");
        List<ServerEntity> lessServers = new ArrayList<>();

        for (int i = 0; i < range; i++) {
            for (ServerEntity en : temporary) {
                if (aR.get() == null || en.getData().getPlayers().size() < aR.get().getData().getPlayers().size()) aR.set(en);
            }
            lessServers.add(aR.get());
            temporary.remove(aR.get());
            aR.set(null);
        }
        return lessServers;
    }

}
