package main

import (
	"context"
	"flag"
	"google.golang.org/grpc"
	"google.golang.org/grpc/health"
	healthgrpc "google.golang.org/grpc/health/grpc_health_v1"
	"log"
	"minki.io/mappy/api"
	pb "minki.io/mappy/proto"
	"minki.io/mappy/storage"
	"net"
)

func main() {
	port := flag.String("port", ":3000", "the port the server is running on")
	flag.Parse()

	lis, err := net.Listen("tcp", *port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	store, err := storage.NewMongoStorage(context.Background())
	if err != nil {
		panic(err)
	}

	s := grpc.NewServer()
	healthcheck := health.NewServer()
	healthgrpc.RegisterHealthServer(s, healthcheck)
	pb.RegisterMappyServiceServer(s, api.NewServer(store))

	log.Println("Server is running on port", *port)

	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
