package net.evan.masterapp.utils.rabbit;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RabbitEventManager {

    private String queue;
    private RabbitMQ rabbitMQ;
    private List<RabbitListener> rabbitListeners;

    public RabbitEventManager(String queue, RabbitMQ rabbitMQ) {
        this.queue = queue;
        this.rabbitMQ = rabbitMQ;
        this.rabbitListeners = new ArrayList<>();
        this.init();
    }

    private void init(){
        try {
            rabbitMQ.getChannel().queueDeclare(queue, false, false, false, new HashMap<>());
            rabbitMQ.getChannel().basicConsume(queue, true, new RabbitHandler(this), s -> {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerListener(RabbitListener rabbitListener){
        this.rabbitListeners.add(rabbitListener);
    }

    public void sendMessage(String target, String channel, String... messages){
        try {
            RabbitMessage container = new RabbitMessage(this.queue, channel, Arrays.asList(messages));
            this.rabbitMQ.getChannel().basicPublish("", target, new AMQP.BasicProperties(), (new Gson()).toJson(container).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public final List<RabbitListener> getRabbitListeners() {
        return rabbitListeners;
    }

}
