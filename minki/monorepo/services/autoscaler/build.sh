GOOS=linux GOARCH=amd64 go build -o autoscaler
docker build --platform linux/amd64 -t minki-autoscaler .