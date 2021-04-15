package net.evan.masterapp.listeners;

import com.google.gson.reflect.TypeToken;
import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.entities.ServerDataEntity;
import net.evan.masterapp.entities.ServerEntity;
import net.evan.masterapp.packets.PacketMovePlayer;
import net.evan.masterapp.packets.PacketRegisterServer;
import net.evan.masterapp.queues.QueuePlayer;
import net.evan.masterapp.utils.rabbit.RabbitEventHandler;
import net.evan.masterapp.utils.rabbit.RabbitListener;
import net.evan.masterapp.utils.rabbit.RabbitMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ListenerHeartbeat implements RabbitListener {

    @RabbitEventHandler(channel = "heartbeat")
    public void receive(RabbitMessage message) {
        MasterAPP app = MasterAPP.getApp();
        String serverName = message.getMessages().get(0);

        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        List<String> players = app.getMasterProvider().getGson().fromJson(message.getMessages().get(1), listType);

        ServerEntity entity = app.getEntitiesManager().retrieveServer(serverName);

        //List<ServerEntity> entities = app.getCacheManager().getServerEntities();

        if (entity.getData().getStatus() == ServerDataEntity.Status.STARTING) {
            entity.getData().setStartedAt(System.currentTimeMillis());
            entity.getData().setStatus(ServerDataEntity.Status.WAITING_FOR_PLAYERS);
            entity.getData().setPlayers(players);
            app.getEntitiesManager().getProxyEntityList().forEach(p -> new PacketRegisterServer(app, serverName).invoke(p.getName()));
            checkStartingServers(app, entity);
            checkWaitingPlayers(app, entity);
            return;
        }

        entity.getData().setPlayers(players);
        checkServerClose(app, entity);
        checkWaitingPlayers(app, entity);

    }

    private long getDateDiff(long l1, long l2, TimeUnit timeUnit) {
        long diffInMillies = l2 - l1;
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    private void checkStartingServers(MasterAPP app, ServerEntity entity) {
        if (app.getQueueManager().getStartingServerMap().size() == 0) return;

        for (Map.Entry entry : app.getQueueManager().getStartingServerMap().entrySet()) {
            String template = (String) entry.getKey();
            ServerEntity en = (ServerEntity) entry.getValue();
            if (en.getName().equals(entity.getName())) {
                app.getQueueManager().getStartingServerMap().remove(template);
            }
        }
    }

    private void checkWaitingPlayers(MasterAPP app, ServerEntity entity) {
        for (Map.Entry entry : app.getQueueManager().getWaitingFor().entrySet()) {
            QueuePlayer player = (QueuePlayer) entry.getKey();
            ServerEntity en = (ServerEntity) entry.getValue();

            if (entity.getName().equals(en.getName())) {
                this.teleportTo(app, player, en);
                app.getQueueManager().getWaitingFor().remove(player);
            }
        }
    }

    private void checkServerClose(MasterAPP app, ServerEntity entity) {
        if (!(entity.getData().getTemplate().equalsIgnoreCase("hub")) && (!(entity.getData().getTemplate().equalsIgnoreCase("auth")))) {
            if (entity.getData().getPlayers().size() == 0 && entity.getData().getStatus() == ServerDataEntity.Status.WAITING_FOR_PLAYERS) {
                if (getDateDiff(entity.getData().getStartedAt(), System.currentTimeMillis(), TimeUnit.SECONDS) >= 60) {
                    app.getLogger().info("The server " + entity.getName() + " contains 0 players and is started since more than 60 seconds");

                    entity.getData().setStatus(ServerDataEntity.Status.FINISHED);
                    app.getLogger().debug("Closing the server " + entity.getName());

                    //TODO: Broadcast in server that he's going to shutdown
                    app.getEntitiesManager().shutdown(entity);

                }
            }
        }
    }

    private void teleportTo(MasterAPP app, QueuePlayer queuePlayer, ServerEntity entity) {
        new PacketMovePlayer(app, queuePlayer.getPlayerName(), "proxyTest", entity.getName()).invoke("proxyTest");
    }

}
