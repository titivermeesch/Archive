package storage

import (
	"context"
	"minki.io/playermanager/types"
)

type Storage interface {
	StorePlayerJoinEvent(ctx context.Context, event types.JoinEvent) error
}
