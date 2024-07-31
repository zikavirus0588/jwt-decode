resource "aws_iam_role" "ecs_task_execution" {
  name = "ecsTaskExecutionRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_policy" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
  role       = aws_iam_role.ecs_task_execution.name
}

resource "aws_ecs_task_definition" "jwt_decode" {
  family                   = "jwt-decode-app"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 1024
  memory                   = 2048
  execution_role_arn       = aws_iam_role.ecs_task_execution.arn

  container_definitions = <<DEFINITION
[
  {
    "image": "${var.ecs_container_image}",
    "cpu": 1024,
    "memory": 2048,
    "name": "jwt-decode-app",
    "networkMode": "awsvpc",
    "environment": ${jsonencode(var.jwt_decode_env_vars)},
    "portMappings": [
      {
        "containerPort": 8001,
        "hostPort": 8001
      }
    ]
  }
]
DEFINITION
}

resource "aws_ecs_cluster" "main" {
  name = "jwt-decode-cluster"
}

resource "aws_ecs_service" "jwt_decode" {
  name            = "jwt-decode-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.jwt_decode.arn
  desired_count   = var.app_count
  launch_type     = "FARGATE"

  network_configuration {
    security_groups = [aws_security_group.jwt_decode_task.id]
    subnets         = aws_subnet.private.*.id
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.jwt_decode.id
    container_name   = "jwt-decode-app"
    container_port   = 8001
  }

  depends_on = [aws_lb_listener.jwt_decode]
}
