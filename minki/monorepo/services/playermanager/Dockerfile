FROM golang:1.22-alpine3.19

RUN apk add --no-cache make gcc g++ dnsmasq git

WORKDIR /app/playermanager

COPY . .
COPY packages/events ../../packages/events
COPY packages/cache ../../packages/cache

RUN cd ../../packages/events && go mod download
RUN cd ../../packages/cache && go mod download

RUN CGO_ENABLED=1 GOOS=linux GOARCH=amd64 go build -tags musl -o playermanager

EXPOSE 3000

CMD [ "./playermanager" ]