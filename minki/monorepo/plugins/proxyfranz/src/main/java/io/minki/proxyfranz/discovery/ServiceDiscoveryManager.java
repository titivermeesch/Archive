package io.minki.proxyfranz.discovery;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceList;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.minki.proxyfranz.ProxyFranz;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServiceDiscoveryManager {
    private final ProxyServer proxy;
    private final ProxyFranz plugin;

    public ServiceDiscoveryManager(ProxyFranz plugin, ProxyServer proxy) {
        this.plugin = plugin;
        this.proxy = proxy;
    }

    public void startServiceDiscovery() {
        proxy.getScheduler().buildTask(plugin, () -> {
            try(KubernetesClient client = new KubernetesClientBuilder().build()) {
                CustomResourceDefinition gameserverCrd = client
                        .apiextensions()
                        .v1()
                        .customResourceDefinitions()
                        .load(plugin.getClass().getResourceAsStream("/gameserver.yml"))
                        .get();
                CustomResourceDefinitionContext gameserverCrdContextFromCrd = CustomResourceDefinitionContext.fromCrd(gameserverCrd);

                GenericKubernetesResourceList gameServerList = client.genericKubernetesResources(gameserverCrdContextFromCrd).inNamespace("default").list();

                ArrayList<GameServer> gameServers = new ArrayList<>();
                for (GenericKubernetesResource item : gameServerList.getItems()) {
                    // TODO: Very ugly, there is a way with fabric8 to create a GameServer POJO instead
                    Map<String, Object> status = (Map<String, Object>) item.getAdditionalProperties().get("status");
                    ArrayList<Map<String, Object>> ports = (ArrayList<Map<String, Object>>) status.get("ports");
                    if (ports == null) {
                        continue;
                    }

                    gameServers.add(new GameServer(item.getMetadata().getName(), status.get("address").toString(), Integer.parseInt(ports.get(0).get("port").toString())));
                }

                // First unregister all servers that are not available anymore
                proxy.getAllServers().forEach(proxyServer -> {
                    // TODO: Ideally this is not needed and lobbies are managed through fleets as well
                    if (proxyServer.getServerInfo().getName().equals("lobby")) {
                        return;
                    }
                    GameServer matchedGameServer = gameServers.stream().filter(gameServer -> gameServer.getName().equals(proxyServer.getServerInfo().getName())).findFirst().orElse(null);
                    if (matchedGameServer == null) {
                        System.out.println("Unregistering server " + proxyServer.getServerInfo().getName());
                        proxy.unregisterServer(proxyServer.getServerInfo());
                    }
                });

                gameServers.forEach(gameServer -> {
                    // Check if the server is already registered
                    if (proxy.getServer(gameServer.getName()).isPresent()) {
                        return;
                    }

                    // Don't register other proxies
                    if (gameServer.getName().contains("proxy-")) {
                        return;
                    }

                    System.out.println("Registering server " + gameServer.getName());
                    ServerInfo serverInfo = new ServerInfo(gameServer.getName(), new InetSocketAddress(gameServer.getAddress(), gameServer.getPort()));
                    proxy.registerServer(serverInfo);
                });
            }
        }).repeat(5, TimeUnit.SECONDS).schedule();
    }
}
