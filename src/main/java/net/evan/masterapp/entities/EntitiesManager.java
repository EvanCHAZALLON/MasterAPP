package net.evan.masterapp.entities;

import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.packets.PacketUnregisterServer;
import net.evan.masterapp.socket.SocketBalancer;
import net.evan.masterapp.utils.DockerService;

import java.util.*;

public class EntitiesManager {

    private final MasterAPP app;

    private SocketBalancer socketBalancer;
    private int listeningPort;

    private Set<DockerService> dockerServices = new HashSet<>();

    private List<ServerEntity> serverEntityList;
    private List<ProxyEntity> proxyEntityList;

    private final String PROXIES_LIST_KEY = "proxies";

    public EntitiesManager(MasterAPP app, int listeningPort) {
        this.app = app;
        this.listeningPort = listeningPort;
        this.serverEntityList = new ArrayList<>();
        this.proxyEntityList = new ArrayList<>();
        this.socketBalancer = new SocketBalancer(app, listeningPort);
        app.getLogger().info("EntitiesManager was succefully loaded.");
    }

    public ServerEntity launch(ServerEntity entity) {
        DockerService service = new DockerService(entity.getName(), entity.getData().getTemplate() + ":v1.0", "network");
        service.setHostname(entity.getName());
        this.runSwarmService(service);
        this.register(entity);
        MasterAPP.getApp().getLogger().info("Launched " + entity.getName());

        dockerServices.add(service);
        return entity;
    }

    public void launch(ProxyEntity entity) {
        DockerService service = new DockerService(entity.getName(), "proxy:v1.0", "network");
        service.setTargetPort(25577); //Default port
        service.setPublishedPort(entity.getPort());
        service.setHostname(entity.getName());
        this.runSwarmService(service);
        this.register(entity);
        MasterAPP.getApp().getLogger().info("Launched " + entity.getName());

        MasterAPP.getApp().getMasterProvider().getRedis().zadd(PROXIES_LIST_KEY, 0, entity.getName());
        dockerServices.add(service);
    }

    public void shutdown(ServerEntity entity) {
        //TODO:REMAKE app.getCacheManager().removeServerEntity(entity);
        dockerServices.stream().filter(service1 -> service1.getName().equals(entity.getName())).findFirst().ifPresent(this::removeService);
        serverEntityList.stream().filter(s -> s.getName().equals(entity.getName())).findFirst().ifPresent(srv -> serverEntityList.remove(srv));
        proxyEntityList.forEach(proxy -> new PacketUnregisterServer(MasterAPP.getApp(), entity.getName()).invoke(proxy.getName()));
        MasterAPP.getApp().getLogger().success("The server " + entity.getName() + " has succefully shutted down.");
    }

    private void register(ServerEntity entity) {
        if (!isRegistered(entity.getName())) {
            entity.getData().setStatus(ServerDataEntity.Status.STARTING);
            serverEntityList.add(entity);
        }
    }

    private void register(ProxyEntity entity) {
        if (!isRegistered(entity)) proxyEntityList.add(entity);
    }

    public boolean isRegistered(String entityName) {
        return serverEntityList.stream().anyMatch(s -> s.getName().equals(entityName));
    }

    public boolean isRegistered(ProxyEntity entity) {
        return proxyEntityList.stream().anyMatch(p -> p.getName().equals(entity.getName()));
    }

    public ServerEntity retrieveServer(String entityName) {
        Optional<ServerEntity> e = serverEntityList.stream().filter(s -> s.getName().equals(entityName)).findAny();
        return e.orElse(null);
    }

    public ProxyEntity retrieveProxy(String proxyName) {
        Optional<ProxyEntity> e = proxyEntityList.stream().filter(s -> s.getName().equals(proxyName)).findAny();
        return e.orElse(null);
    }

    public List<ServerEntity> getByType(String template) {
        List<ServerEntity> serverEntities = new ArrayList<>();
        for (ServerEntity server : getServerEntityList()) {
            if (server.getData().getTemplate().equalsIgnoreCase(template)) {
                serverEntities.add(server);
            }
        }
        return serverEntities;
    }

    public ProxyEntity getProxyByName(String name) {
        return proxyEntityList.stream().filter(proxy -> proxy.getName().equals(name)).findFirst().orElse(null);
    }

    private void runSwarmService(DockerService service) {
        MasterAPP.getApp().getMasterProvider().getDockerClient().createServiceCmd(service.toSwarmService()).exec();
    }

    private void removeService(DockerService service) {
        MasterAPP.getApp().getMasterProvider().getDockerClient().removeServiceCmd(service.getName()).exec();
        dockerServices.remove(service);
    }

    public Set<DockerService> getDockerServices() {
        return dockerServices;
    }

    public List<ProxyEntity> getProxyEntityList() {
        return proxyEntityList;
    }

    public List<ServerEntity> getServerEntityList() {
        return serverEntityList;
    }

    public SocketBalancer getSocketBalancer() {
        return socketBalancer;
    }
}
