package kafka

import (
	"fmt"
	cKafka "github.com/confluentinc/confluent-kafka-go/v2/kafka"
	"github.com/joho/godotenv"
	"os"
)

type KafkaClient struct {
	Consumer *cKafka.Consumer
	Handlers map[string]MessageEventHandler
}

type MessageEventHandler func(message *cKafka.Message) error

func NewKafkaProducer() (*cKafka.Producer, error) {
	config, err := getKafkaConfig()
	if err != nil {
		return nil, err
	}

	return cKafka.NewProducer(&config)
}

func NewKafkaClient() (*KafkaClient, error) {
	config, err := getKafkaConfig()
	if err != nil {
		return nil, err
	}

	consumer, err := cKafka.NewConsumer(&config)
	if err != nil {
		return nil, err
	}

	return &KafkaClient{Consumer: consumer, Handlers: map[string]MessageEventHandler{}}, nil
}

func (k *KafkaClient) RegisterTopicConsumer(topic string, handler MessageEventHandler) error {
	err := k.Consumer.Subscribe(topic, nil)
	if err != nil {
		return err
	}

	if k.Handlers[topic] != nil {
		return fmt.Errorf("handler for topic %s already exists", topic)
	}

	k.Handlers[topic] = handler
	return nil
}

func (k *KafkaClient) StartIngestion() {
	run := true
	for run {
		e := k.Consumer.Poll(1000)
		switch ev := e.(type) {
		case *cKafka.Message:
			topic := *ev.TopicPartition.Topic
			handler, ok := k.Handlers[topic]
			if ok {
				err := handler(ev)
				if err != nil {
					fmt.Fprintf(os.Stderr, "Error handling message: %v\n", err)
				}
			}
		case cKafka.Error:
			fmt.Fprintf(os.Stderr, "%% Error: %v\n", ev)
			run = false
		}
	}
}

func (k *KafkaClient) Close() {
	k.Consumer.Close()
}

func getKafkaConfig() (cKafka.ConfigMap, error) {
	err := godotenv.Load()
	if err != nil {
		return nil, err
	}

	kafkaBootstrapServer := os.Getenv("KAFKA_BOOTSTRAP_SERVER")
	kafkaUsername := os.Getenv("KAFKA_USERNAME")
	kafkaPassword := os.Getenv("KAFKA_PASSWORD")
	kafkaGroupId := os.Getenv("KAFKA_GROUP_ID")

	// TODO: Validate that all envs are there

	return cKafka.ConfigMap{
		"bootstrap.servers":  kafkaBootstrapServer,
		"security.protocol":  "SASL_SSL",
		"sasl.mechanism":     "PLAIN",
		"sasl.username":      kafkaUsername,
		"sasl.password":      kafkaPassword,
		"session.timeout.ms": 45000,
		"group.id":           kafkaGroupId,
		"auto.offset.reset":  "earliest",
	}, nil
}
