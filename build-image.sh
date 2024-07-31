#!/bin/bash

# Packing the project
mvn clean package -DskipTests -q

# Fetch AWS Account ID
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query "Account" --output text)

# Fetch AWS Region
AWS_REGION=$(aws configure get region)

# Echo the values (for debugging purposes)
echo "AWS Account ID: $AWS_ACCOUNT_ID"
echo "AWS Region: $AWS_REGION"

# Retrieve an authentication token and authenticate your Docker client to your registry.
aws ecr get-login-password --region "${AWS_REGION}" | docker login --username AWS --password-stdin "${AWS_ACCOUNT_ID}".dkr.ecr."${AWS_REGION}".amazonaws.com

# Define the ECR repository name and image tag
REPOSITORY_NAME="jwt-decode-des"
IMAGE_TAG="latest"

# Construct the ECR image URI
ECR_IMAGE_URI="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${REPOSITORY_NAME}:${IMAGE_TAG}"

# Echo the ECR image URI
echo "ECR Image URI: $ECR_IMAGE_URI"

# Build docker image
docker build -t ${REPOSITORY_NAME} .

# Tag image with latest version
docker tag ${REPOSITORY_NAME}:${IMAGE_TAG} "${ECR_IMAGE_URI}"

# Push image to ECR
docker push "${ECR_IMAGE_URI}"
