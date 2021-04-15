package net.evan.masterapp.entities;

public class ProxyEntity {

    private String name;
    private int port;

    private ProxyDataEntity proxyDataEntity;

    public ProxyEntity(String name, int port, ProxyDataEntity proxyDataEntity) {
        this.name = name;
        this.port = port;
        this.proxyDataEntity = proxyDataEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ProxyDataEntity getProxyDataEntity() {
        return proxyDataEntity;
    }

    public void setProxyDataEntity(ProxyDataEntity proxyDataEntity) {
        this.proxyDataEntity = proxyDataEntity;
    }
}
