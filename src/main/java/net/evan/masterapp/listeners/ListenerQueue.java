package net.evan.masterapp.listeners;

import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.queues.QueuePlayer;
import net.evan.masterapp.utils.rabbit.RabbitEventHandler;
import net.evan.masterapp.utils.rabbit.RabbitListener;
import net.evan.masterapp.utils.rabbit.RabbitMessage;

public class ListenerQueue implements RabbitListener {

    @RabbitEventHandler(channel = "queue")
    public void receive(RabbitMessage rabbitMessage) {
        MasterAPP app = MasterAPP.getApp();

        String playerName = rabbitMessage.getMessages().get(1);
        String queueTemplate = rabbitMessage.getMessages().get(2);

        if (!app.getQueueManager().exists(queueTemplate)) return;

        if (rabbitMessage.getMessages().get(0).equals("join")) {
            if (app.getQueueManager().isPlayerInQueue(queueTemplate, playerName)) return;
            app.getQueueManager().addPlayerToQueue(queueTemplate, new QueuePlayer(playerName, "default"));

        } else if (rabbitMessage.getMessages().get(0).equals("leave")) {
            if (!app.getQueueManager().isPlayerInQueue(queueTemplate, playerName)) return;
            app.getQueueManager().removePlayerFromQueue(queueTemplate, playerName);
        }

    }
}
