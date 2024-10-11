package storage

import (
	"context"
	"minki.io/mappy/types"
)

type Storage interface {
	SaveMap(ctx context.Context, mapData types.Map) error
	GetMap(ctx context.Context, mapID string) (types.Map, error)
}
