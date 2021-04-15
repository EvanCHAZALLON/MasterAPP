package net.evan.masterapp.runnables;

import net.evan.masterapp.MasterAPP;

import java.util.concurrent.TimeUnit;

public class RunnableManager {

    private final MasterAPP app;

    public RunnableManager(MasterAPP app) {
        this.app = app;
        app.getLogger().info("RunnableManager was succefully loaded.");
    }

    public void run(Runnable runnable, int initialDelay, int delay, TimeUnit timeUnit) {
        app.getMasterProvider().getExecutorService().scheduleAtFixedRate(runnable, initialDelay, delay, timeUnit);
        app.getLogger().info("Running " + runnable.getClass().getName() + ".");
    }

}
