package net.evan.masterapp.utils.rabbit;

import java.util.List;

public class RabbitMessage extends RabbitRequest {

    private String sender;
    private String channel;
    private List<String> messages;


    public RabbitMessage(String sender, String channel, List<String> messages) {
        this.sender = sender;
        this.channel = channel;
        this.messages = messages;
    }

    public final String getSender() { return sender; }
    public final String getChannel() { return channel; }
    public final List<String> getMessages() { return messages; }

}
