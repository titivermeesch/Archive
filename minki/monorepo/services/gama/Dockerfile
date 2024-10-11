FROM golang:1.22-alpine3.19

ARG PORT

WORKDIR /app
COPY . .
#
#RUN go mod download
#RUN GOOS=linux GOARCH=amd64 go build -o autoscaler

EXPOSE 8080

CMD ["./gama"]