variable "aws_region" {
  description = "The AWS region to deploy the resources in"
  type        = string
  default     = "us-east-2"
}

variable "aws_profile" {
  description = "The AWS profile to use"
  type        = string
  default     = "default"
}

variable "ecs_container_image" {
  description = "The ECS container image to use"
  type        = string
  default     = "218208237654.dkr.ecr.us-east-2.amazonaws.com/jwt-decode-des:latest"
}

variable "app_count" {
  description = "Number of instances of the application"
  type        = number
  default     = 1
}

variable "jwt_decode_env_vars" {
  description = "Environment variables for JWT Decode application"
  type        = list(map(string))
  default     = [{
    "name"  = "SWAGGER_PROTOCOL",
    "value" = "http"
  }, {
    "name"  = "SWAGGER_BASE_HOST",
    "value" = "localhost"
  }]
}
