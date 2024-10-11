GOOS=linux GOARCH=amd64 go build -o mappy
docker build --platform linux/amd64 -t minki-mappy .