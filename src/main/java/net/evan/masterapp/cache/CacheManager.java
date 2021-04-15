package net.evan.masterapp.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.evan.masterapp.MasterAPP;
import net.evan.masterapp.entities.ProxyEntity;
import net.evan.masterapp.entities.ServerEntity;
import org.eclipse.jetty.server.Server;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class CacheManager {

    private final MasterAPP app;
    private final Jedis jedis;
    private final Gson gson;

    private final String SERVERS_KEY = "servers";
    private final String PROXIES_KEY = "proxies";

    public CacheManager(MasterAPP app) {
        this.app = app;
        this.jedis = app.getMasterProvider().getRedis();
        this.gson = app.getMasterProvider().getGson();
    }

    public void addServerEntity(ServerEntity entity) {
        List<ServerEntity> actualList = getServerEntities();
        actualList.add(entity);
        updateServerEntities(actualList);
    }

    public void addProxyEntity(ProxyEntity entity) {
        List<ProxyEntity> actualList = getProxyEntities();
        actualList.add(entity);
        updateProxiesEntities(actualList);
    }

    public void removeServerEntity(ServerEntity entity) {
        List<ServerEntity> actualList = getServerEntities();
        actualList.stream().filter(srv -> srv.getName().equals(entity.getName())).findFirst().ifPresent(actualList::remove);
        updateServerEntities(actualList);
    }

    public void removeProxyEntity(ProxyEntity entity) {
        List<ProxyEntity> actualList = getProxyEntities();
        actualList.stream().filter(proxy -> proxy.getName().equals(entity.getName())).findFirst().ifPresent(actualList::remove);
        updateProxiesEntities(actualList);
    }

    private void updateServerEntities(List<ServerEntity> entities) {
        jedis.del(SERVERS_KEY);
        jedis.set(SERVERS_KEY, gson.toJson(entities));
    }

    private void updateProxiesEntities(List<ProxyEntity> entities) {
        jedis.del(PROXIES_KEY);
        jedis.set(PROXIES_KEY, gson.toJson(entities));
    }

    public List<ServerEntity> getServerEntities() {
        if (!existServer()) return new ArrayList<>();
        return gson.fromJson(jedis.get(SERVERS_KEY), new TypeToken<ArrayList<ServerEntity>>(){}.getType());
    }

    public List<ProxyEntity> getProxyEntities() {
        if (!existProxies()) return new ArrayList<>();
        return gson.fromJson(jedis.get(PROXIES_KEY), new TypeToken<ArrayList<ProxyEntity>>(){}.getType());
    }

    private boolean existServer() {
        return jedis.get(SERVERS_KEY) != null;
    }

    private boolean existProxies() {
        return jedis.get(PROXIES_KEY) != null;
    }

}
