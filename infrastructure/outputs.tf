output "load_balancer_ip" {
  value = aws_lb.default.dns_name
}

output "alb_dns" {
  value = aws_lb.default.arn
}

output "api_endpoint" {
  value = aws_apigatewayv2_api.api.api_endpoint
}