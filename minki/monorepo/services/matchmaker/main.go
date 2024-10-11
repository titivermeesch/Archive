package main

import (
	"cmp"
	"context"
	"encoding/json"
	"fmt"
	cKafka "github.com/confluentinc/confluent-kafka-go/v2/kafka"
	"github.com/go-co-op/gocron"
	"github.com/joho/godotenv"
	"github.com/redis/go-redis/v9"
	"google.golang.org/grpc"
	"log"
	events "minki.io/events"
	pbGama "minki.io/gama/proto"
	pb "minki.io/queuemanager/proto"
	"os"
	"slices"
	"time"
)

func main() {
	teleportTopic := "teleport_requests"
	ctx := context.Background()
	err := godotenv.Load()
	if err != nil {
		panic(err)
	}

	rdb := redis.NewClient(&redis.Options{
		Addr:     os.Getenv("REDIS_URL"),
		Password: os.Getenv("REDIS_PASSWORD"),
		DB:       0,
	})

	if pong := rdb.Ping(ctx); pong.String() != "ping: PONG" {
		panic("Failed to connect to redis")
	}

	qConn, err := grpc.Dial("queuemanager.default.svc.cluster.local:3000", grpc.WithInsecure())
	if err != nil {
		panic(err)
	}

	defer qConn.Close()
	queueManager := pb.NewQueueManagerServiceClient(qConn)

	gConn, err := grpc.Dial("gama.default.svc.cluster.local:3000", grpc.WithInsecure())
	if err != nil {
		panic(err)
	}

	defer gConn.Close()
	gama := pbGama.NewGamaServiceClient(gConn)

	producer, err := events.NewKafkaProducer()
	if err != nil {
		panic(err)
	}

	s := gocron.NewScheduler(time.UTC)
	_, err = s.Every(5).Seconds().Do(func() {
		log.Println("Matchmaking...")
		queues, err := queueManager.GetAllQueues(ctx, &pb.Empty{})
		if err != nil {
			log.Println(err)
			return
		}
		log.Println(fmt.Sprintf("Found %d queues", len(queues.Queues)))

		for _, queue := range queues.Queues {
			log.Println(fmt.Sprintf("Processing queue %s", queue.GetGame()))
			game := queue.GetGame()
			mapName := queue.GetMap()
			players := queue.GetPlayers()

			req := &pbGama.GetAvailableGameServersRequest{}

			if game != "" {
				req.Game = &game
			}

			if mapName != "" && mapName != "*" {
				req.Map = &mapName
			}

			serversResponse, err := gama.GetAvailableGameServers(ctx, req)
			if err != nil {
				log.Println(err)
				return
			}

			servers := serversResponse.Servers

			log.Println(fmt.Sprintf("Found %d servers", len(servers)))

			if len(servers) == 0 {
				log.Println(fmt.Sprintf("No servers available for game %s %s", game, mapName))
				continue
			}

			slices.SortFunc(servers, func(i, j *pbGama.GameServer) int {
				return cmp.Compare(len(j.Players), len(i.Players))
			})

			log.Println(fmt.Sprintf("Found %d players", len(players)))

			for _, player := range players {
				candidateServer := servers[0]
				availableSlots := candidateServer.GetMaxPlayers() - int32(len(candidateServer.GetPlayers()))

				log.Println(fmt.Sprintf("Available slots for server %s: %d", candidateServer.Id, availableSlots))

				if availableSlots > 0 {
					marshalledRequest, err := json.Marshal(struct {
						Uuid   string `json:"uuid"`
						Server string `json:"server"`
					}{
						Server: candidateServer.Id,
						Uuid:   player,
					})
					if err != nil {
						log.Println(err)
						return
					}

					err = producer.Produce(&cKafka.Message{
						TopicPartition: cKafka.TopicPartition{Topic: &teleportTopic, Partition: cKafka.PartitionAny},
						Key:            []byte(""),
						Value:          marshalledRequest,
					}, nil)
					if err != nil {
						log.Println(err)
					}

					availableSlots--

					// If slots for current server are full, remove it from the list and continue with next available server
					if availableSlots == 0 {
						servers = servers[1:]
					}

					// If there are no more servers available, stop matchmaking. The autoscaler should do its job and provide more
					if len(servers) == 0 {
						break
					}
				}
			}
		}
	})
	if err != nil {
		panic(err)
	}

	s.StartAsync()

	log.Println("Matchmaker is running")

	select {}
}
