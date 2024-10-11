package io.minki.proxyfranz.events;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class KafkaManager {
    private final Producer<String, String> producer;

    public KafkaManager(Properties config) {
        this.producer = new KafkaProducer<>(config);
    }

    public void produce(String topic, String key, String value) {
        producer.send(new ProducerRecord<>(topic, key, value));
    }
}
