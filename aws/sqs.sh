aws --region us-east-1 --endpoint-url=http://localhost:4566 sqs create-queue --queue-name="test-dead-letter-queue"
aws --region us-east-1 --endpoint-url=http://localhost:4566 sqs create-queue --queue-name="test-queue" \
--attributes '{
  "RedrivePolicy": "{\"deadLetterTargetArn\":\"arn:aws:sqs:us-east-1:000000000000:test-dead-letter-queue\",\"maxReceiveCount\":\"1000\",\"VisibilityTimeout\":\"5\"}",
  "MessageRetentionPeriod": "259200"
  }'

