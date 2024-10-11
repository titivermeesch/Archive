package io.minki.proxyfranz;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import io.github.cdimascio.dotenv.Dotenv;
import io.minki.proxyfranz.commands.LobbyCommand;
import io.minki.proxyfranz.discovery.ServiceDiscoveryManager;
import io.minki.proxyfranz.events.ChatListeners;
import io.minki.proxyfranz.events.KafkaManager;
import io.minki.proxyfranz.events.PlayerConnectionListener;
import io.minki.proxyfranz.events.PlayerTeleportHandler;
import io.minki.proxyfranz.server.ServerManager;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.expiry.ExpiryPolicy;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Plugin(id = "proxyfranz", name = "ProxyFranz", version = "1.0.0", description = "ProxyFranz", authors = {"Minki"})
public class ProxyFranz {
    private final ProxyServer server;
    private final Logger logger;
    private final KafkaManager kafkaManager;
    private final ServerManager serverManager;
    private final Cache<String, String> cache;

    @Inject
    public ProxyFranz(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        this.kafkaManager = new KafkaManager(getKafkaConfig());
        this.serverManager = new ServerManager(server);
        this.cache = new Cache2kBuilder<String, String>() {
        }
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .eternal(false)
                .sharpExpiry(true)
                .build();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new PlayerConnectionListener(this.server, this.kafkaManager));
        server.getEventManager().register(this, new ChatListeners(this, server));
        new ServiceDiscoveryManager(this, server).startServiceDiscovery();
        new PlayerTeleportHandler(getKafkaConfig(), server);
        registerCommands();
        logger.info("ProxyFranz has been initialized");
    }

    private void registerCommands() {
        CommandManager commandManager = server.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder("lobby")
                .aliases("l", "hub", "leave")
                .plugin(this)
                .build();
        commandManager.register(commandMeta, new LobbyCommand(serverManager));
    }

    private Properties getKafkaConfig() {
        Dotenv dotenv = Dotenv.load();
        Thread.currentThread().setContextClassLoader(null);
        Thread.currentThread().setContextClassLoader(KafkaProducer.class.getClassLoader());
        Properties config = new Properties();

        config.setProperty("bootstrap.servers", dotenv.get("KAFKA_BOOTSTRAP_SERVER"));
        config.setProperty("security.protocol", "SASL_SSL");
        config.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='" + dotenv.get("KAFKA_USERNAME") + "' password='" + dotenv.get("KAFKA_PASSWORD") + "';");
        config.setProperty("sasl.mechanism", "PLAIN");
        config.setProperty("client.dns.lookup", "use_all_dns_ips");
        config.setProperty("session.timeout.ms", "45000");
        config.setProperty("acks", "all");
        config.setProperty("group.id", "proxy");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        return config;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public Cache<String, String> getCache() {
        return cache;
    }
}
