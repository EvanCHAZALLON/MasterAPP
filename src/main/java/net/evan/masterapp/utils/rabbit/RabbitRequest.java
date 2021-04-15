package net.evan.masterapp.utils.rabbit;

public class RabbitRequest {

    private RabbitEventManager rabbitEventManager;
    private RabbitMessage rabbitMessage;

    void init(RabbitEventManager rabbitEventManager, RabbitMessage rabbitMessage){
        this.rabbitEventManager = rabbitEventManager;
        this.rabbitMessage = rabbitMessage;
    }

    public void sendResponse(String... messages){
        rabbitEventManager.sendMessage(rabbitMessage.getSender(), rabbitMessage.getChannel(), messages);
    }

    public void sendMessage(String target, String channel, String... messages){
        rabbitEventManager.sendMessage(target, channel, messages);
    }

}
