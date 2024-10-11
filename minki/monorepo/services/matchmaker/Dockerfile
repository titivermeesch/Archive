FROM golang:1.22-alpine3.19

RUN apk add --no-cache make gcc g++ dnsmasq git

WORKDIR /app/matchmaker

COPY . .
COPY packages/events ../../packages/events
COPY services/gama ../../services/gama
COPY services/queuemanager ../../services/queuemanager

RUN cd ../../packages/events && go mod download
RUN cd ../../services/gama && go mod download
RUN cd ../../services/queuemanager && go mod download

RUN CGO_ENABLED=1 GOOS=linux GOARCH=amd64 go build -tags musl -o matchmaker

EXPOSE 3000

CMD [ "./matchmaker" ]