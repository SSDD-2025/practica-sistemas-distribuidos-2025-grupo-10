#!/bin/bash

# Define variables for the image name and tag
IMAGE_NAME="wenwenshrek/docker-compose.prod.yml"
IMAGE_TAG="0.1.0"
COMPOSE_FILE="docker-compose.prod.yml"

# Construct the docker compose command with variables
sudo docker compose -f "$COMPOSE_FILE" publish --with-env "$IMAGE_NAME":"$IMAGE_TAG"

echo "Published $IMAGE_NAME:$IMAGE_TAG using $COMPOSE_FILE"
