package players

type PlayerSettings struct {
	MatchmakingPreference string `json:"matchmaking_preference" bson:"matchmaking_preference"`
}

type Player struct {
	Uuid        string         `json:"uuid"`
	Username    string         `json:"username"`
	Rank        string         `json:"rank"`
	Level       int            `json:"level"`
	Experience  int            `json:"experience"`
	FirstJoined int64          `json:"first_joined" bson:"first_joined"`
	LastJoined  int64          `json:"last_joined" bson:"last_joined"`
	Settings    PlayerSettings `json:"settings"`
}
