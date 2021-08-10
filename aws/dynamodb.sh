aws --region us-east-1 --endpoint-url=http://localhost:4566 \
  dynamodb create-table \
  --table-name test-table \
  --attribute-definitions \
  AttributeName=Id,AttributeType=S \
  AttributeName=Sort,AttributeType=S \
  AttributeName=Name,AttributeType=S \
  --key-schema \
  AttributeName=Id,KeyType=HASH \
  AttributeName=Sort,KeyType=RANGE \
  --global-secondary-indexes \
  "IndexName=test-name-index,KeySchema=[{AttributeName=Name,KeyType=HASH}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5}" \
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5