package api

import (
	"context"
	"go.mongodb.org/mongo-driver/mongo"
	"minki.io/cache"
	"minki.io/playermanager/friends"
	"minki.io/playermanager/players"
	pb "minki.io/playermanager/proto"
)

type Server struct {
	PlayerManager  *players.Manager
	FriendsManager *friends.Manager
	*pb.UnimplementedPlayerManagerServiceServer
}

var _ pb.PlayerManagerServiceServer = &Server{}

func NewServer(db *mongo.Database, c *cache.Cache) (*Server, error) {
	playerManager, err := players.NewManager(db, c)
	if err != nil {
		return nil, err
	}

	friendsManager, err := friends.NewManager(db, c)
	if err != nil {
		return nil, err
	}

	return &Server{
		PlayerManager:  playerManager,
		FriendsManager: friendsManager,
	}, nil
}

func (s *Server) GetPlayer(ctx context.Context, in *pb.GetPlayerRequest) (*pb.GetPlayerResponse, error) {
	player, err := s.PlayerManager.GetPlayerByUUID(ctx, in.GetUuid())
	if err != nil {
		return nil, err
	}

	return &pb.GetPlayerResponse{
		Uuid:        player.Uuid,
		Username:    player.Username,
		Level:       int32(player.Level),
		Experience:  int32(player.Experience),
		Rank:        player.Rank,
		FirstJoined: player.FirstJoined,
		LastJoined:  player.LastJoined,
	}, nil
}

func (s *Server) UpdatePlayer(ctx context.Context, request *pb.UpdatePlayerRequest) (*pb.Empty, error) {
	//TODO implement me
	panic("implement me")
}

func (s *Server) GetPlayerFriends(ctx context.Context, request *pb.GetPlayerFriendsRequest) (*pb.GetPlayerFriendsResponse, error) {
	//TODO implement me
	panic("implement me")
}

func (s *Server) AddFriend(ctx context.Context, request *pb.AddFriendRequest) (*pb.Empty, error) {
	//TODO implement me
	panic("implement me")
}
