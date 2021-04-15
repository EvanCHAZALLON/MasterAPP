package net.evan.masterapp.entities;

public class ServerEntity {

    private String name;
    private ServerDataEntity serverDataEntity;

    public ServerEntity(String name, ServerDataEntity serverDataEntity) {
        this.name = name;
        this.serverDataEntity = serverDataEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServerDataEntity getData() {
        return serverDataEntity;
    }

    public void setServerDataEntity(ServerDataEntity serverDataEntity) {
        this.serverDataEntity = serverDataEntity;
    }
}
