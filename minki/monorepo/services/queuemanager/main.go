package main

import (
	"context"
	"flag"
	"github.com/joho/godotenv"
	"github.com/redis/go-redis/v9"
	"google.golang.org/grpc"
	"google.golang.org/grpc/health"
	healthgrpc "google.golang.org/grpc/health/grpc_health_v1"
	"log"
	"log/slog"
	"minki.io/queuemanager/api"
	pb "minki.io/queuemanager/proto"
	"net"
	"os"
)

func main() {
	err := godotenv.Load()
	port := flag.String("port", ":3000", "the port the server is running on")
	flag.Parse()

	lis, err := net.Listen("tcp", *port)
	if err != nil {
		panic(err)
	}

	rdb := redis.NewClient(&redis.Options{
		Addr:     os.Getenv("REDIS_URL"),
		Password: os.Getenv("REDIS_PASSWORD"),
		DB:       0,
	})

	if pong := rdb.Ping(context.Background()); pong.String() != "ping: PONG" {
		panic("Failed to connect to redis")
	}

	s := grpc.NewServer()
	apiServer := api.NewServer(rdb)
	if err != nil {
		panic(err)
	}
	healthcheck := health.NewServer()
	healthgrpc.RegisterHealthServer(s, healthcheck)
	pb.RegisterQueueManagerServiceServer(s, apiServer)

	slog.Info("Server is running", "port", *port)

	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
