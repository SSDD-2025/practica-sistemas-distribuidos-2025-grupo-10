#!/bin/bash

set -e

#Full name of the image with tag
FULL_IMAGE_NAME="wenwenshrek/shop:1.0.0"

DOCKERFILE_PATH="Dockerfile"

echo "Construyendo imagen Docker: $FULL_IMAGE_NAME desde $DOCKERFILE_PATH"

# Builds the image with the specific Dockerfile
sudo docker build -t $FULL_IMAGE_NAME -f $DOCKERFILE_PATH ..

echo "Imagen construida correctamente: $FULL_IMAGE_NAME"
