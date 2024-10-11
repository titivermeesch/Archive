module minki.io/matchmaker

go 1.22.2

require (
	github.com/go-co-op/gocron v1.37.0
	github.com/joho/godotenv v1.5.1
	github.com/redis/go-redis/v9 v9.5.1
	google.golang.org/grpc v1.63.2
	minki.io/events v0.0.0-00010101000000-000000000000
	minki.io/gama v0.0.0-00010101000000-000000000000
	minki.io/queuemanager v0.0.0-00010101000000-000000000000
)

require (
	github.com/cespare/xxhash/v2 v2.2.0 // indirect
	github.com/confluentinc/confluent-kafka-go/v2 v2.3.0 // indirect
	github.com/dgryski/go-rendezvous v0.0.0-20200823014737-9f7001d12a5f // indirect
	github.com/google/uuid v1.6.0 // indirect
	github.com/robfig/cron/v3 v3.0.1 // indirect
	go.uber.org/atomic v1.9.0 // indirect
	golang.org/x/net v0.23.0 // indirect
	golang.org/x/sys v0.18.0 // indirect
	golang.org/x/text v0.14.0 // indirect
	google.golang.org/genproto/googleapis/rpc v0.0.0-20240227224415-6ceb2ff114de // indirect
	google.golang.org/protobuf v1.33.0 // indirect
)

replace minki.io/queuemanager => ../../services/queuemanager

replace minki.io/gama => ../../services/gama

replace minki.io/events => ../../packages/events
