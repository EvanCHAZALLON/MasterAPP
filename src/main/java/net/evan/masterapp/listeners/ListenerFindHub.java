package net.evan.masterapp.listeners;

import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.entities.ServerEntity;
import net.evan.masterapp.packets.PacketMovePlayer;
import net.evan.masterapp.utils.rabbit.RabbitEventHandler;
import net.evan.masterapp.utils.rabbit.RabbitListener;
import net.evan.masterapp.utils.rabbit.RabbitMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerFindHub implements RabbitListener {

    @RabbitEventHandler(channel = "findHub")
    public void receive(RabbitMessage rabbitMessage) {
        MasterAPP app = MasterAPP.getApp();

        String playerName = rabbitMessage.getMessages().get(0);
        String playerProxy = rabbitMessage.getMessages().get(1);

        ServerEntity lessConnect = getLessConnect(app.getEntitiesManager().getByType("hub"));
        app.getLogger().debug("Attempting to move player on " + lessConnect.getName() + " (" + lessConnect.getData().getPlayers().size() + " players)");
        new PacketMovePlayer(app, playerName, playerProxy, lessConnect.getName()).invoke(playerProxy);
        app.getLogger().debug("Success!");
    }

    private ServerEntity getLessConnect(List<ServerEntity> entityList) {
        Map<ServerEntity, Integer> serversWithConnect = new HashMap<>();
        entityList.forEach(s -> serversWithConnect.put(s, s.getData().getPlayers().size()));
        return Collections.min(serversWithConnect.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

}
