package cache

import (
	"context"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"log"
	"time"
)

type Manager struct {
	Database   *mongo.Database
	Collection *mongo.Collection
	Options    *ManagerOptions
}

type ManagerOptions struct {
	Database   *mongo.Database
	Collection string
	TTL        time.Duration
	Encode     func(interface{}) (string, error)
	Decode     func(string, interface{}) error
	Cache      ICache
}

func NewManager(options *ManagerOptions) (*Manager, error) {
	return &Manager{
		Collection: options.Database.Collection(options.Collection),
		Options:    options,
	}, nil
}

func (m *Manager) FindOne(ctx context.Context, filter primitive.D, cacheKey string, result *interface{}) error {
	//exists, err := m.Options.Cache.Exists(ctx, cacheKey)
	//if err != nil {
	//	return err
	//}

	exists := 0

	if exists == 1 {
		log.Println("cache findOne hit")
		value, err := m.Options.Cache.Get(ctx, cacheKey)
		if err != nil {
			return err
		}
		err = m.Options.Decode(value, &result)
		if err != nil {
			return err
		}

		return nil
	}

	log.Println("cache findOne miss, fetching from db")
	log.Println("cacheKey", cacheKey)
	log.Println("filter", filter)
	res := m.Collection.FindOne(ctx, filter)

	if res.Err() != nil {
		return res.Err()
	}

	log.Println("res", res)
	raw, err := res.Raw()
	if err != nil {
		return err
	}

	log.Println("raw", raw)

	err = res.Decode(result)
	if err != nil {
		return err
	}

	encoded, err := m.Options.Encode(result)
	if err != nil {
		return err
	}

	err = m.Options.Cache.Set(ctx, cacheKey, encoded, m.Options.TTL)
	if err != nil {
		return err
	}

	return nil
}
