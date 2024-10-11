GOOS=linux GOARCH=amd64 go build -o gama
docker build --platform linux/amd64 -t minki-gama .