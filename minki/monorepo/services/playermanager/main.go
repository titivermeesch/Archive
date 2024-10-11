package main

import (
	"context"
	"flag"
	"github.com/joho/godotenv"
	"google.golang.org/grpc"
	"google.golang.org/grpc/health"
	healthgrpc "google.golang.org/grpc/health/grpc_health_v1"
	"log"
	"minki.io/cache"
	eventsPkg "minki.io/events"
	"minki.io/playermanager/api"
	"minki.io/playermanager/events"
	pb "minki.io/playermanager/proto"
	"minki.io/playermanager/storage"
	"net"
	"os"
)

func main() {
	ctx := context.Background()
	port := flag.String("port", ":3000", "the port the server is running on")
	flag.Parse()

	err := godotenv.Load()
	if err != nil {
		panic(err)
	}

	store, err := storage.NewMongoStorage(ctx)
	if err != nil {
		panic(err)
	}

	kafka, err := eventsPkg.NewKafkaClient()
	if err != nil {
		panic(err)
	}

	eventHandler := events.NewEventHandler(kafka.Consumer, store)
	err = kafka.RegisterTopicConsumer("player_connection", eventHandler.HandlePlayerJoinEvent)
	if err != nil {
		panic(err)
	}

	c := cache.NewCache(os.Getenv("REDIS_URL"), os.Getenv("REDIS_PASSWORD"))

	lis, err := net.Listen("tcp", *port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	apiServer, err := api.NewServer(store.Database, c)
	if err != nil {
		panic(err)
	}

	s := grpc.NewServer()
	healthcheck := health.NewServer()
	healthgrpc.RegisterHealthServer(s, healthcheck)
	pb.RegisterPlayerManagerServiceServer(s, apiServer)

	log.Println("Server is running on port", *port)

	go kafka.StartIngestion()

	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}

}
