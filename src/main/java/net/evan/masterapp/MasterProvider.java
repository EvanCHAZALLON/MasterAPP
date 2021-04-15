package net.evan.masterapp;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Swarm;
import com.github.dockerjava.core.DockerClientBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.evan.masterapp.listeners.ListenerFindHub;
import net.evan.masterapp.listeners.ListenerHeartbeat;
import net.evan.masterapp.listeners.ListenerQueue;
import net.evan.masterapp.utils.rabbit.RabbitEventManager;
import net.evan.masterapp.utils.rabbit.RabbitMQ;
import redis.clients.jedis.Jedis;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MasterProvider {

    private final DockerClient dockerClient;
    private final Gson gson;
    private final RabbitMQ rabbitMQ;
    private final Jedis jedis;
    private final RabbitEventManager rabbitEventManager;
    private final ScheduledExecutorService executorService;

    public MasterProvider() {
        this.dockerClient = DockerClientBuilder.getInstance("unix:///var/run/docker.sock").build();
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();

        this.rabbitMQ = new RabbitMQ("guest", "guest", "rabbitmq");
        this.rabbitMQ.connect();

        this.jedis = new Jedis("redis", 6379);

        this.rabbitEventManager = new RabbitEventManager("master", rabbitMQ);
        this.registerListeners();

        this.executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    private void registerListeners() {
        rabbitEventManager.registerListener(new ListenerHeartbeat());
        rabbitEventManager.registerListener(new ListenerQueue());
        rabbitEventManager.registerListener(new ListenerFindHub());
    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }

    public Swarm getDockerSwarm() {
        return dockerClient.inspectSwarmCmd().exec();
    }

    public Gson getGson() {
        return gson;
    }

    public RabbitMQ getRabbitMQ() {
        return rabbitMQ;
    }

    public Jedis getRedis() {
        return jedis;
    }

    public RabbitEventManager getRabbitEventManager() {
        return rabbitEventManager;
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }
}
