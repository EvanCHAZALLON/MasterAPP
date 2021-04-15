package net.evan.masterapp.utils.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class RabbitMQ {

    private ConnectionFactory connectionFactory;
    private Channel channel;
    private Connection connection;

    public RabbitMQ(String username, String password, String host, int port) {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setPassword(password);
        connectionFactory.setUsername(username);
    }

    public RabbitMQ(String username, String password, String host) {
        this(username, password, host, 5672);
    }

    public RabbitMQ connect(){
        try{
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }

    public RabbitMQ close(){
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Channel getChannel() {
        return channel;
    }

}
