package storage

import (
	"context"
	"github.com/joho/godotenv"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"minki.io/playermanager/types"
	"os"
)

type MongoPlayerCollections struct {
	Players *mongo.Collection
}

type MongoStorage struct {
	Database    *mongo.Database
	collections *MongoPlayerCollections
}

var _ Storage = &MongoStorage{}

func NewMongoStorage(ctx context.Context) (*MongoStorage, error) {
	err := godotenv.Load()
	if err != nil {
		return nil, err
	}

	client, err := mongo.Connect(ctx, options.Client().ApplyURI(os.Getenv("MONGO_URI")))

	return &MongoStorage{
		Database: client.Database(os.Getenv("MONGO_DATABASE")),
		collections: &MongoPlayerCollections{
			Players: client.Database(os.Getenv("MONGO_DATABASE")).Collection("players"),
		},
	}, nil
}

func (m *MongoStorage) StorePlayerJoinEvent(ctx context.Context, event types.JoinEvent) error {
	filter := bson.D{{"uuid", event.Uuid}}
	set := bson.D{
		{"$set", bson.D{
			{"username", event.Username},
			{"ip", event.Ip},
			{"last_joined", event.Timestamp},
		}},
		{"$setOnInsert", bson.D{
			{"uuid", event.Uuid},
			{"level", 0},
			{"experience", 0},
			{"rank", "default"},
			{"first_joined", event.Timestamp},
		}},
	}

	_, err := m.collections.Players.UpdateOne(ctx, filter, set, options.Update().SetUpsert(true))

	return err
}
