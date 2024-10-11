package friends

import (
	"context"
	"encoding/json"
	"fmt"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"minki.io/cache"
	"time"
)

type Friend struct {
}

type Manager struct {
	Collection *mongo.Collection
	Cache      *cache.Manager
}

func NewManager(db *mongo.Database, c cache.ICache) (*Manager, error) {
	cacheManager, err := cache.NewManager(&cache.ManagerOptions{
		Database:   db,
		Collection: "friends",
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
		Collection: db.Collection("friends"),
		Cache:      cacheManager,
	}, nil
}

func (m *Manager) AddFriend() {

}

func (m *Manager) RemoveFriend() {

}

func (m *Manager) GetFriends() {

}

func (m *Manager) GetFriend(ctx context.Context, uuid1 string, uuid2 string) (*Friend, error) {
	cacheKey := fmt.Sprintf("%s:%s", uuid1, uuid2)

	friend := &Friend{}
	filter := bson.D{
		{"$or", bson.A{
			bson.D{{"uuid1", uuid1}, {"uuid2", uuid2}},
			bson.D{{"uuid1", uuid2}, {"uuid2", uuid1}},
		}},
	}
	err := m.Cache.FindOne(ctx, filter, cacheKey, friend)
	if err != nil {
		return nil, err
	}

	return friend, nil
}
