package players

import (
	"context"
	"encoding/json"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"minki.io/cache"
	"time"
)

type Manager struct {
	Collection *mongo.Collection
	Cache      *cache.Manager
}

func NewManager(db *mongo.Database, c cache.ICache) (*Manager, error) {
	cacheManager, err := cache.NewManager(&cache.ManagerOptions{
		Database:   db,
		Collection: "players",
		TTL:        120 * time.Second,
		Cache:      c,
		Encode: func(data interface{}) (string, error) {
			res, err := json.Marshal(data)
			if err != nil {
				return "", err
			}

			return string(res), nil
		},
		Decode: func(data string, result interface{}) error {
			err := json.Unmarshal([]byte(data), &result)
			if err != nil {
				return err
			}
			return nil
		},
	})

	if err != nil {
		return nil, err
	}

	return &Manager{
		Collection: db.Collection("players"),
		Cache:      cacheManager,
	}, nil
}

func (m *Manager) GetPlayerByUUID(ctx context.Context, uuid string) (*Player, error) {
	player := &Player{}
	filter := bson.D{{"uuid", uuid}}
	err := m.Cache.FindOne(ctx, filter, uuid, player)
	if err != nil {
		return nil, err
	}

	return player, nil
}
