package net.evan.masterapp.utils.rabbit;

import com.google.gson.Gson;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class RabbitHandler implements DeliverCallback {

    private RabbitEventManager rabbitEventManager;
    private Gson gson;

    RabbitHandler(RabbitEventManager rabbitEventManager) {
        this.rabbitEventManager = rabbitEventManager;
        this.gson = new Gson();
    }

    @Override
    public void handle(String consumerTag, Delivery message) {
        try {
            String s = new String(message.getBody(), StandardCharsets.UTF_8);
            RabbitMessage rabbitMessage = gson.fromJson(s, RabbitMessage.class);
            rabbitMessage.init(rabbitEventManager, rabbitMessage);
            callListeners(gson.fromJson(s, RabbitMessage.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callListeners(RabbitMessage rabbitMessage) throws Exception  {
        for (RabbitListener rabbitListener : rabbitEventManager.getRabbitListeners()) {
            for (Method method : rabbitListener.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(RabbitEventHandler.class)) {
                    if (method.getParameterTypes().length == 1 && rabbitMessage.getClass().equals(method.getParameterTypes()[0])) {
                        RabbitEventHandler rabbitEventHandler = method.getAnnotation(RabbitEventHandler.class);
                        if(rabbitEventHandler.channel().equals(rabbitMessage.getChannel())) {
                            method.invoke(rabbitListener, rabbitMessage);
                        }
                    }
                }
            }
        }
    }

}
