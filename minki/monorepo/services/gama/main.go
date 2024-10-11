package main

import (
	"agones.dev/agones/pkg/client/clientset/versioned"
	"flag"
	"google.golang.org/grpc"
	"google.golang.org/grpc/health"
	healthgrpc "google.golang.org/grpc/health/grpc_health_v1"
	"k8s.io/client-go/rest"
	"log"
	"minki.io/gama/api"
	pb "minki.io/gama/proto"
	"net"
)

func main() {
	port := flag.String("port", ":3000", "the port the server is running on")
	flag.Parse()

	config, err := rest.InClusterConfig()
	if err != nil {
		panic(err)
	}
	agonesClient, err := versioned.NewForConfig(config)
	if err != nil {
		panic(err)
	}

	lis, err := net.Listen("tcp", *port)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	s := grpc.NewServer()
	healthcheck := health.NewServer()
	healthgrpc.RegisterHealthServer(s, healthcheck)
	pb.RegisterGamaServiceServer(s, api.NewServer(agonesClient))

	log.Println("Server is running on port", *port)

	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
