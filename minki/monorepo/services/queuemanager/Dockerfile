FROM golang:1.22-alpine3.19

WORKDIR /app

COPY . .
#RUN GOOS=linux GOARCH=amd64 go build -o queuemanager

EXPOSE 3000

CMD [ "./queuemanager" ]