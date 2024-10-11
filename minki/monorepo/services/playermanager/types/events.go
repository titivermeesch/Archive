package types

type JoinEvent struct {
	Uuid      string `json:"uuid"`
	Username  string `json:"username"`
	Ip        string `json:"ip"`
	Timestamp int64  `json:"timestamp"`
}
