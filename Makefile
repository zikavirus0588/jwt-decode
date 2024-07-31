.PHONY: docker-compose-local-up docker-compose-local-down docker-build ecr-login ecr-push tf-init tf-plan tf-apply tf-destroy

docker-compose-local-up:
	@echo "Building containers..."
	@docker compose -f ./docker-compose-local.yaml up -d --build || { echo "Building local containers failed"; exit 1; }
	@echo "You may now test your application"

docker-compose-local-down:
	@echo "Removing containers..."
	@docker compose -f ./docker-compose-local.yaml down -v || { echo "Removing local containers failed"; exit 1; }

docker-build:
	@echo "Building the Docker image..."
	@IMAGE_NAME=jwt-decode TAG=latest && docker build -t $${IMAGE_NAME}:$${TAG} -f Dockerfile . || { echo "Docker build failed"; exit 1; }

ecr-login:
	@echo "Logging in to AWS ECR..."
	@AWS_ACCOUNT_ID=$$(aws sts get-caller-identity --query "Account" --output text) && \
	 AWS_REGION=$$(aws configure get region) && \
	 aws ecr get-login-password --region $${AWS_REGION} | docker login --username AWS --password-stdin $${AWS_ACCOUNT_ID}.dkr.ecr.$${AWS_REGION}.amazonaws.com || \
	 { echo "Docker login failed"; exit 1; }
# Build the Docker image

ecr-push: ecr-login
	@echo "Pushing the Docker image to AWS ECR..."
	@AWS_ACCOUNT_ID=$$(aws sts get-caller-identity --query "Account" --output text) && \
    	AWS_REGION=$$(aws configure get region) && \
    	IMAGE_NAME=jwt-decode TAG=latest ECR_REPO_NAME=$${IMAGE_NAME}-des && \
		docker tag $${IMAGE_NAME}:$${TAG} $${AWS_ACCOUNT_ID}.dkr.ecr.$${AWS_REGION}.amazonaws.com/$${ECR_REPO_NAME}:$${TAG} && \
		docker push $${AWS_ACCOUNT_ID}.dkr.ecr.$${AWS_REGION}.amazonaws.com/$${ECR_REPO_NAME}:$${TAG} || { echo "ECR push image failed"; exit 1; }

# Initialize the Terraform configuration
tf-init:
	@echo "Initializing Terraform..."
	@cd infrastructure && \
		AWS_ACCOUNT_ID=$$(aws sts get-caller-identity --query "Account" --output text) && \
		AWS_PROFILE=default && \
		AWS_REGION=$$(aws configure get region) && \
		IMAGE_NAME=jwt-decode && \
		TAG=latest && \
		ECR_REPO_NAME=$${IMAGE_NAME}-des && \
		ECR_IMAGE_URI=$${AWS_ACCOUNT_ID}.dkr.ecr.$${AWS_REGION}.amazonaws.com/$${ECR_REPO_NAME}:$${TAG} && \
		tofu init || { echo "Initializing terraform failed"; exit 1; };

# Plan the Terraform deployment
tf-plan:
	@echo "Planning infrastructure..."
	@cd infrastructure && \
		AWS_ACCOUNT_ID=$$(aws sts get-caller-identity --query "Account" --output text) && \
		AWS_PROFILE=default && \
		AWS_REGION=$$(aws configure get region) && \
		IMAGE_NAME=jwt-decode && \
		TAG=latest && \
		ECR_REPO_NAME=$${IMAGE_NAME}-des && \
		ECR_IMAGE_URI=$${AWS_ACCOUNT_ID}.dkr.ecr.$${AWS_REGION}.amazonaws.com/$${ECR_REPO_NAME}:$${TAG} && \
		tofu plan -out=tfplan || { echo "Planning infrastructure failed"; exit 1; };

# Apply the Terraform deployment
tf-apply:
	@echo "Applying plan to construct infrastructure..."
	@cd infrastructure && \
		AWS_ACCOUNT_ID=$$(aws sts get-caller-identity --query "Account" --output text) && \
		AWS_PROFILE=default && \
		AWS_REGION=$$(aws configure get region) && \
		IMAGE_NAME=jwt-decode && \
		TAG=latest && \
		ECR_REPO_NAME=$${IMAGE_NAME}-des && \
		ECR_IMAGE_URI=$${AWS_ACCOUNT_ID}.dkr.ecr.$${AWS_REGION}.amazonaws.com/$${ECR_REPO_NAME}:$${TAG} && \
		tofu apply tfplan || { echo "Applying plan to construct infrastructure failed"; exit 1; };

# Destroy the Terraform infrastructure
tf-destroy:
	@echo "Destroying infrastructure..."
	@cd infrastructure && \
		AWS_ACCOUNT_ID=$$(aws sts get-caller-identity --query "Account" --output text) && \
		AWS_PROFILE=default && \
		AWS_REGION=$$(aws configure get region) && \
		IMAGE_NAME=jwt-decode && \
		TAG=latest && \
		ECR_REPO_NAME=$${IMAGE_NAME}-des && \
		ECR_IMAGE_URI=$${AWS_ACCOUNT_ID}.dkr.ecr.$${AWS_REGION}.amazonaws.com/$${ECR_REPO_NAME}:$${TAG} && \
		tofu destroy -auto-approve || { echo "Destroying infrastructure failed"; exit 1; };