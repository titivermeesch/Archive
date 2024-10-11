# QueueManager

Service response for handling different player queues. This service should be the only one manipulating the kv store queues

## Internals

QueueManager uses Redis to keep track of all the players that are currently queued