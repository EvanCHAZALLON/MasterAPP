package net.evan.masterapp;

import net.evan.masterapp.cache.CacheManager;
import net.evan.masterapp.entities.*;
import net.evan.masterapp.logger.Logger;
import net.evan.masterapp.packets.PacketManager;
import net.evan.masterapp.queues.QueueEntity;
import net.evan.masterapp.queues.QueueManager;
import net.evan.masterapp.runnables.RunnableHeartbeat;
import net.evan.masterapp.runnables.RunnableHub;
import net.evan.masterapp.runnables.RunnableManager;
import net.evan.masterapp.runnables.RunnableQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MasterAPP {

    public static MasterAPP APP;

    private final ExecutorService executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    private final Logger logger = new Logger();
    private final MasterProvider masterProvider = new MasterProvider();
    private final EntitiesManager entitiesManager = new EntitiesManager(this, 25565);
    private final PacketManager packetManager = new PacketManager(this);
    private final RunnableManager runnableManager = new RunnableManager(this);
    private final QueueManager queueManager = new QueueManager(this);

    public MasterAPP() {
        logger.info("Launching MasterAPP");

        APP = this;

        entitiesManager.launch(new ProxyEntity("proxy-01", 25566, new ProxyDataEntity()));
        entitiesManager.launch(new ProxyEntity("proxy-02", 25567, new ProxyDataEntity()));

        entitiesManager.launch(new ServerEntity("hubTest", new ServerDataEntity("hub", 0, 10, false)));
        entitiesManager.launch(new ServerEntity("auth", new ServerDataEntity("auth", 0, 800, false)));

        runnableManager.run(new RunnableHeartbeat(this), 0, 1, TimeUnit.SECONDS);
        runnableManager.run(new RunnableQueue(this), 0, 1, TimeUnit.SECONDS);
        runnableManager.run(new RunnableHub(this), 10, 10, TimeUnit.SECONDS);

        queueManager.addQueue(new QueueEntity("game"));
    }

    public static void main(String[] args) {
        new MasterAPP();
    }

    public static MasterAPP getApp() {
        return APP;
    }

    public MasterProvider getMasterProvider() {
        return masterProvider;
    }

    public Logger getLogger() {
        return logger;
    }

    public EntitiesManager getEntitiesManager() {
        return entitiesManager;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public RunnableManager getRunnableManager() {
        return runnableManager;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    /*public CacheManager getCacheManager() {
        return cacheManager;
    }*/

}
