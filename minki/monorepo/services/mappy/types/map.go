package types

type MapGridFsEntry struct {
	ID       string `bson:"_id"`
	Filename string `bson:"filename"`
	Metadata struct {
		Game       string `bson:"game"`
		Map        string `bson:"map"`
		Version    string `bson:"version"`
		MinPlayers int    `bson:"min_players"`
		MaxPlayers int    `bson:"max_players"`
	}
}

type Map struct {
	ID   string `bson:"_id"`
	Name string `bson:"name"`
	Game string `bson:"game"`
}
