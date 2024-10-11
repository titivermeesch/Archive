package storage

import (
	"context"
	"github.com/joho/godotenv"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/gridfs"
	"go.mongodb.org/mongo-driver/mongo/options"
	"minki.io/mappy/types"
	"os"
)

type MongoStorage struct {
	Database *mongo.Database
	Bucket   *gridfs.Bucket
}

func NewMongoStorage(ctx context.Context) (*MongoStorage, error) {
	err := godotenv.Load()
	if err != nil {
		return nil, err
	}

	client, err := mongo.Connect(ctx, options.Client().ApplyURI(os.Getenv("MONGO_URI")))
	if err != nil {
		return nil, err
	}

	database := client.Database(os.Getenv("MONGO_DATABASE"))
	opts := options.GridFSBucket().SetName("game_maps")
	bucket, err := gridfs.NewBucket(database, opts)
	if err != nil {
		return nil, err
	}

	return &MongoStorage{
		Database: database,
		Bucket:   bucket,
	}, nil
}

func (m *MongoStorage) SaveMap(ctx context.Context, mapData types.Map) error {
	return nil
}
