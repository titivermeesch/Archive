GOOS=linux GOARCH=amd64 go build -o queuemanager
docker build --platform linux/amd64 -t minki-queuemanager .