package events

import (
	"context"
	"encoding/json"
	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
	"log"
	"minki.io/playermanager/storage"
	"minki.io/playermanager/types"
)

type EventHandler struct {
	Consumer *kafka.Consumer
	Store    storage.Storage
}

func NewEventHandler(consumer *kafka.Consumer, store storage.Storage) *EventHandler {
	return &EventHandler{
		Consumer: consumer,
		Store:    store,
	}
}

func (e *EventHandler) HandlePlayerJoinEvent(message *kafka.Message) error {
	ctx := context.Background()

	log.Println("Handling player join event")

	var joinEvent types.JoinEvent
	err := json.Unmarshal(message.Value, &joinEvent)
	if err != nil {
		return err
	}

	err = e.Store.StorePlayerJoinEvent(ctx, joinEvent)
	if err != nil {
		log.Println(err)
		return err
	}

	return nil
}
