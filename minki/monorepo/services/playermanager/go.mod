module minki.io/playermanager

go 1.22.2

require (
	github.com/confluentinc/confluent-kafka-go/v2 v2.3.0
	github.com/joho/godotenv v1.5.1
	go.mongodb.org/mongo-driver v1.15.0
	google.golang.org/grpc v1.54.0
	google.golang.org/protobuf v1.30.0
	minki.io/cache v0.0.0-00010101000000-000000000000
	minki.io/events v0.0.0-00010101000000-000000000000
)

require (
	github.com/cespare/xxhash/v2 v2.2.0 // indirect
	github.com/dgryski/go-rendezvous v0.0.0-20200823014737-9f7001d12a5f // indirect
	github.com/golang/protobuf v1.5.3 // indirect
	github.com/golang/snappy v0.0.1 // indirect
	github.com/klauspost/compress v1.13.6 // indirect
	github.com/montanaflynn/stats v0.0.0-20171201202039-1bf9dbcd8cbe // indirect
	github.com/redis/go-redis/v9 v9.5.1 // indirect
	github.com/xdg-go/pbkdf2 v1.0.0 // indirect
	github.com/xdg-go/scram v1.1.2 // indirect
	github.com/xdg-go/stringprep v1.0.4 // indirect
	github.com/youmark/pkcs8 v0.0.0-20181117223130-1be2e3e5546d // indirect
	golang.org/x/crypto v0.17.0 // indirect
	golang.org/x/net v0.10.0 // indirect
	golang.org/x/sync v0.1.0 // indirect
	golang.org/x/sys v0.15.0 // indirect
	golang.org/x/text v0.14.0 // indirect
	google.golang.org/genproto v0.0.0-20230331144136-dcfb400f0633 // indirect
)

replace minki.io/events => ../../packages/events

replace minki.io/cache => ../../packages/cache
