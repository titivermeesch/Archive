# Minki Monorepo

Welcome to the Minki monorepo. This repo contains all the mayor packages, plugins and services required to manage the
Minki project.

## Local Development

As a lot of services relly on the internal Kubernetes API, it is recommended to run a local Kubernetes cluster to make
the development experience better.

### Through a remote Windows machine

When trying to run

### Cluster setup

- kubectl create clusterrolebinding default-view --clusterrole=cluster-admin --serviceaccount=default:default
