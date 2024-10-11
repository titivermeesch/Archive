package api

import (
	"context"
	"encoding/binary"
	"fmt"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo/options"
	"io"
	"log"
	"math/rand"
	pb "minki.io/mappy/proto"
	"minki.io/mappy/storage"
	"minki.io/mappy/types"
	"os"
)

type Server struct {
	store *storage.MongoStorage
	*pb.UnimplementedMappyServiceServer
}

func NewServer(store *storage.MongoStorage) *Server {
	return &Server{
		store: store,
	}
}

func (s *Server) UploadMap(ctx context.Context, req *pb.UploadMapRequest) (*pb.UploadMapResponse, error) {
	file, err := os.Open(req.ZipPath)
	if err != nil {
		return nil, err
	}

	uploadOpts := options.GridFSUpload().SetMetadata(bson.D{
		{"game", req.Game},
		{"map", req.Map},
		{"version", req.Version},
		{"min_players", req.MinPlayers},
		{"max_players", req.MaxPlayers},
	})

	objectID, err := s.store.Bucket.UploadFromStream(fmt.Sprintf("%s_%s.zip", req.Game, req.Map), io.Reader(file),
		uploadOpts)
	if err != nil {
		return nil, err
	}

	return &pb.UploadMapResponse{
		Id: objectID.Hex(),
	}, nil
}

func (s *Server) GetMap(ctx context.Context, req *pb.GetMapRequest) (*pb.GetMapResponse, error) {
	mapEntry := types.MapGridFsEntry{}
	err := s.store.Database.Collection("game_maps.files").FindOne(ctx, bson.D{{"metadata.game", req.Game}, {"metadata.map", req.Map}}).Decode(&mapEntry)
	if err != nil {
		return nil, err
	}
	id, err := primitive.ObjectIDFromHex(mapEntry.ID)
	if err != nil {
		return nil, err
	}
	downloadStream, err := s.store.Bucket.OpenDownloadStream(id)
	if err != nil {
		panic(err)
	}
	fileBytes := make([]byte, 1024)
	if _, err := downloadStream.Read(fileBytes); err != nil {
		panic(err)
	}

	return &pb.GetMapResponse{
		Map: &pb.Map{
			Map:        mapEntry.Metadata.Map,
			Game:       mapEntry.Metadata.Game,
			Version:    mapEntry.Metadata.Version,
			MinPlayers: int32(mapEntry.Metadata.MinPlayers),
			MaxPlayers: int32(mapEntry.Metadata.MaxPlayers),
			Data:       fileBytes,
		},
	}, nil
}

func (s *Server) ListMaps(ctx context.Context, req *pb.ListMapsRequest) (*pb.ListMapsResponse, error) {
	filter := bson.D{}
	if req.Game != "" {
		filter = bson.D{{"metadata.game", req.GetGame()}}
	}

	maps, err := s.getMaps(ctx, filter)
	if err != nil {
		return nil, err
	}

	return &pb.ListMapsResponse{
		Maps: maps,
	}, nil
}

func (s *Server) GetRandomMap(ctx context.Context, req *pb.GetRandomMapRequest) (*pb.GetRandomMapResponse, error) {
	filter := bson.D{{"metadata.game", req.GetGame()}}

	maps, err := s.getMaps(ctx, filter)
	if err != nil {
		return nil, err
	}

	selectedMap := maps[rand.Intn(len(maps))]
	id, err := primitive.ObjectIDFromHex(selectedMap.Id)
	if err != nil {
		return nil, err
	}
	downloadStream, err := s.store.Bucket.OpenDownloadStream(id)
	if err != nil {
		panic(err)
	}
	fileBytes := make([]byte, downloadStream.GetFile().Length)
	if _, err := downloadStream.Read(fileBytes); err != nil {
		return nil, err
	}

	log.Println(fmt.Sprintf("Map size to download: %d", binary.Size(fileBytes)))

	return &pb.GetRandomMapResponse{
		Map: &pb.Map{
			Id:         selectedMap.Id,
			Map:        selectedMap.Map,
			Game:       selectedMap.Game,
			Version:    selectedMap.Version,
			MinPlayers: selectedMap.MinPlayers,
			MaxPlayers: selectedMap.MaxPlayers,
			Data:       fileBytes,
		},
	}, nil
}

func (s *Server) getMaps(ctx context.Context, filter bson.D) ([]*pb.Map, error) {
	cursor, err := s.store.Database.Collection("game_maps.files").Find(ctx, filter)
	if err != nil {
		return nil, err
	}
	var maps []*pb.Map
	for cursor.Next(ctx) {
		mapEntry := types.MapGridFsEntry{}
		if err := cursor.Decode(&mapEntry); err != nil {
			return nil, err
		}
		maps = append(maps, &pb.Map{
			Id:         mapEntry.ID,
			Map:        mapEntry.Metadata.Map,
			Game:       mapEntry.Metadata.Game,
			Version:    mapEntry.Metadata.Version,
			MinPlayers: int32(mapEntry.Metadata.MinPlayers),
			MaxPlayers: int32(mapEntry.Metadata.MaxPlayers),
		})
	}
	return maps, nil
}
