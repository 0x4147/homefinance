#!/bin/bash

echo "Starting minikube..."
minikube start

echo "Building application image..."
eval $(minikube docker-env)
docker build -t homefinance:latest .

echo "Deploying to Kubernetes..."
kubectl apply -f k8s/

echo "Waiting for deployment to be ready..."
kubectl wait --for=condition=available deployment/mysql --timeout=300s
kubectl wait --for=condition=available deployment/homefinance --timeout=300s

echo "Getting service URLs..."
minikube service homefinance --url

echo "Deployment complete!"