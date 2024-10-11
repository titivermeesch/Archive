package main

import (
	"encoding/json"
	"flag"
	"fmt"
	"io"
	"log"
	"math"
	"net/http"

	autoscalingv1 "agones.dev/agones/pkg/apis/autoscaling/v1"
	"github.com/gin-gonic/gin"
)

var (
	replicaUpperThreshold = 0.7
	replicaLowerThreshold = 0.3
	scaleFactor           = 2.
	minReplicasCount      = int32(2)
)

func main() {
	port := flag.String("port", "8080", "Port to listen on")
	flag.Parse()
	r := gin.Default()
	r.GET("/", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"message": "ok",
		})
	})
	r.POST("/scale", func(c *gin.Context) {
		var faReq autoscalingv1.FleetAutoscaleReview
		res, err := io.ReadAll(c.Request.Body)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{
				"message": "invalid request",
			})
			return
		}

		err = json.Unmarshal(res, &faReq)
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{
				"message": "invalid request",
				"error":   err.Error(),
			})
		}

		faResp := autoscalingv1.FleetAutoscaleResponse{
			Scale:    false,
			Replicas: faReq.Request.Status.Replicas,
			UID:      faReq.Request.UID,
		}
		log.Printf("Amount of current replicas: %d", faReq.Request.Status.Replicas)
		if faReq.Request.Status.Replicas != 0 {
			allocatedPercent := float64(faReq.Request.Status.AllocatedReplicas) / float64(faReq.Request.Status.Replicas)
			if allocatedPercent > replicaUpperThreshold {
				// After scaling we would have percentage of 0.7/2 = 0.35 > replicaLowerThreshold
				// So we won't scale down immediately after scale up
				currentReplicas := float64(faReq.Request.Status.Replicas)
				faResp.Scale = true
				faResp.Replicas = int32(math.Ceil(currentReplicas * scaleFactor))
				log.Printf("Scaling up to %d replicas", faResp.Replicas)
			} else if allocatedPercent < replicaLowerThreshold && faReq.Request.Status.Replicas > minReplicasCount {
				faResp.Scale = true
				faResp.Replicas = int32(math.Ceil(float64(faReq.Request.Status.Replicas) / scaleFactor))
				log.Printf("Scaling down to %d replicas", faResp.Replicas)
			} else if faReq.Request.Status.Replicas < minReplicasCount {
				faResp.Scale = true
				faResp.Replicas = minReplicasCount
				log.Printf("Scaling to minimum replicas count: %d", faResp.Replicas)
			}
		}

		c.Header("Content-Type", "application/json")
		review := &autoscalingv1.FleetAutoscaleReview{
			Request:  faReq.Request,
			Response: &faResp,
		}
		result, _ := json.Marshal(&review)

		c.String(http.StatusOK, string(result))
	})

	log.Printf("running on port %s", *port)
	err := r.Run(fmt.Sprintf(":%s", *port))
	if err != nil {
		panic(err)
	}
}
