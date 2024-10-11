package api

import (
	"agones.dev/agones/pkg/client/clientset/versioned"
	"context"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	pb "minki.io/gama/proto"
	"strconv"
)

type Server struct {
	agones *versioned.Clientset
	pb.UnimplementedGamaServiceServer
}

func NewServer(agones *versioned.Clientset) *Server {
	return &Server{
		agones: agones,
	}
}

func (s *Server) GetAvailableGameServers(ctx context.Context, in *pb.GetAvailableGameServersRequest) (*pb.GetAvailableGameServersResponse, error) {
	options := metav1.ListOptions{
		LabelSelector: "agones.dev/sdk-gameStatus=waiting",
	}
	if in.GetGame() != "" {
		options.LabelSelector = options.LabelSelector + ",game=" + in.GetGame()
	}
	if in.GetMap() != "" {
		options.LabelSelector = options.LabelSelector + ",agones.dev/sdk-map=" + in.GetMap()
	}

	gameServers, err := s.agones.AgonesV1().GameServers("default").List(ctx, options)
	if err != nil {
		return nil, err
	}

	servers := make([]*pb.GameServer, 0)
	for _, gameServer := range gameServers.Items {
		minPlayers, _ := strconv.ParseInt(gameServer.Labels["agones.dev/sdk-minPlayers"], 10, 32)
		maxPlayers, _ := strconv.ParseInt(gameServer.Labels["agones.dev/sdk-maxPlayers"], 10, 32)

		servers = append(servers, &pb.GameServer{
			Id:           gameServer.Name,
			Game:         gameServer.Labels["game"],
			Map:          gameServer.Labels["agones.dev/sdk-map"],
			Status:       gameServer.Labels["gameStatus"],
			Players:      gameServer.Status.Lists["players"].Values,
			MinPlayers:   int32(minPlayers),
			MaxPlayers:   int32(maxPlayers),
			ServerStatus: string(gameServer.Status.State),
		})
	}

	return &pb.GetAvailableGameServersResponse{
		Servers: servers,
	}, nil
}
