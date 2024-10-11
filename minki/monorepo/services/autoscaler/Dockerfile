FROM golang:1.22-alpine3.19

ARG PORT

WORKDIR /app

COPY go.mod .
COPY go.sum .
RUN go mod download

COPY main.go .
RUN GOOS=linux GOARCH=amd64 go build -o autoscaler

EXPOSE 8080

CMD ["./autoscaler"]