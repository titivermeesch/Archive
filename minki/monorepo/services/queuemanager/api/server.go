package api

import (
	"context"
	"fmt"
	"github.com/redis/go-redis/v9"
	"log"
	pb "minki.io/queuemanager/proto"
	"strings"
)

type Server struct {
	kvStore *redis.Client
	*pb.UnimplementedQueueManagerServiceServer
}

// TODO: Remove direct dependency on redis, instead use a storage interface
func NewServer(kvStore *redis.Client) *Server {
	return &Server{
		kvStore: kvStore,
	}
}

func (s *Server) QueuePlayer(ctx context.Context, in *pb.QueuePlayerRequest) (*pb.QueuePlayerResponse, error) {
	queueName := s.getQueueKey(in.GetGame(), in.GetMap())

	if s.kvStore.Exists(ctx, queueName).Val() == 0 {
		log.Printf("Creating queue %s", queueName)
		s.kvStore.SAdd(ctx, "queues:index", queueName)
	}

	s.kvStore.RPush(ctx, queueName, in.GetUuid())
	return &pb.QueuePlayerResponse{}, nil
}

func (s *Server) DequeuePlayer(ctx context.Context, in *pb.DequeuePlayerRequest) (*pb.DequeuePlayerResponse, error) {
	queues := s.kvStore.SMembers(ctx, "queues:index").Val()

	for _, queue := range queues {
		s.kvStore.LRem(ctx, queue, 1, in.GetUuid())
	}

	return &pb.DequeuePlayerResponse{Success: true}, nil
}

func (s *Server) IsQueued(ctx context.Context, in *pb.IsQueuedRequest) (*pb.IsQueuedResponse, error) {
	queues := s.kvStore.SMembers(ctx, "queues:index").Val()

	isQueued := false
	for _, queue := range queues {
		idx, err := s.kvStore.LPos(ctx, queue, in.GetUuid(), redis.LPosArgs{}).Result()
		if err != nil {
			continue
		}

		if idx != -1 {
			isQueued = true
			break
		}
	}

	return &pb.IsQueuedResponse{Queued: isQueued}, nil
}

func (s *Server) GetAllQueues(ctx context.Context, in *pb.Empty) (*pb.GetAllQueuesResponse, error) {
	queueKeys := s.kvStore.SMembers(ctx, "queues:index").Val()

	queues := make([]*pb.Queue, 0)

	for _, queueKey := range queueKeys {
		parts := strings.Split(queueKey, ":")
		queue := &pb.Queue{
			Game:    parts[1],
			Map:     parts[2],
			Players: s.kvStore.LRange(ctx, queueKey, 0, -1).Val(),
		}
		queues = append(queues, queue)
	}

	return &pb.GetAllQueuesResponse{Queues: queues}, nil
}

func (s *Server) getQueueKey(game string, mapName string) string {
	return fmt.Sprintf("queue:%s:%s", game, mapName)
}
