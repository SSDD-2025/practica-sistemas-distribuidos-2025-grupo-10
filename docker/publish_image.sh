#!/bin/bash

set -e

# Complete name with tag
FULL_IMAGE_NAME="wenwenshrek/shop:1.0.0"

echo "Publicando imagen en Docker Hub: $FULL_IMAGE_NAME"

# Publishes the image in Docker Hub
sudo docker push $FULL_IMAGE_NAME

echo "Imagen publicada exitosamente: $FULL_IMAGE_NAME"
