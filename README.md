# Claims Service

A Spring Boot microservice for managing insurance claims with AI-powered summarization using Amazon Bedrock.

## Overview

The Claims Service provides REST API endpoints for retrieving claim information and generating AI-powered summaries of claim details using AWS Lambda and Amazon Bedrock.

## Architecture

- **Framework**: Spring Boot 3.2.10 with Java 21
- **Database**: Amazon DynamoDB for claim data storage
- **File Storage**: Amazon S3 for claim notes and documents
- **AI Processing**: AWS Lambda with Amazon Bedrock (Claude 3 Sonnet)
- **Deployment**: Kubernetes on AWS EKS

## API Endpoints

### Get Claim Details
```
GET /api/v1/claims/{claimId}
```
Retrieves detailed information about a specific claim.

**Response**: Claim object with ID, customer info, status, description, timestamps, and notes.

### Generate Claim Summary
```
POST /api/v1/claims/{claimId}/summarize
```
Generates an AI-powered summary of the claim using Amazon Bedrock.

**Response**: ClaimSummary object with claim ID, generated summary, timestamp, and AI model used.

## Configuration

The service is configured via `application.yml`:

```yaml
server:
  port: 8080

aws:
  region: us-east-1
  dynamodb:
    table-name: claims-table
  s3:
    bucket-name: claims-notes-bucket
  lambda:
    function-name: claims-summarizer-lambda

spring:
  profiles:
    active: default
```

## Building and Running

### Prerequisites
- Java 21
- Maven 3.6+
- AWS CLI configured with appropriate permissions

### Build
```bash
mvn clean compile
```

### Run
```bash
mvn spring-boot:run
```

### Test
```bash
mvn test
```

## AWS Resources Required

1. **DynamoDB Table**: `claims-table`
   - Primary Key: `claimId` (String)

2. **S3 Bucket**: `claims-notes-bucket`
   - Structure: `{claimId}/notes.txt`

3. **Lambda Function**: `claims-summarizer-lambda`
   - Runtime: Python 3.11
   - Handler: `lambda_function.lambda_handler`
   - Environment Variables:
     - `BEDROCK_MODEL_ID`: `anthropic.claude-3-sonnet-20240229-v1:0`

## Data Models

### Claim
```json
{
  "claimId": "string",
  "customerId": "string",
  "status": "string",
  "description": "string",
  "createdDate": "2024-01-01T00:00:00",
  "updatedDate": "2024-01-01T00:00:00",
  "notes": ["string"]
}
```

### ClaimSummary
```json
{
  "claimId": "string",
  "summary": "string",
  "generatedAt": "2024-01-01T00:00:00",
  "modelUsed": "claude-3-sonnet"
}
```

## Error Handling

The service returns appropriate HTTP status codes:
- `200`: Success
- `404`: Claim not found
- `500`: Internal server error

## Monitoring

- Health checks available at `/actuator/health`
- Metrics available at `/actuator/metrics`
- Logs configured with DEBUG level for the `com.example.claims` package

## Security

- AWS IAM roles for service access
- VPC configuration for network isolation
- No authentication implemented (add as needed for production)

## Development

### Project Structure
```
src/
├── main/
│   ├── java/com/example/claims/
│   │   ├── ClaimsApplication.java
│   │   ├── controller/
│   │   │   └── ClaimsController.java
│   │   ├── service/
│   │   │   ├── ClaimsService.java
│   │   │   └── ClaimsServiceImpl.java
│   │   ├── repository/
│   │   │   ├── ClaimsRepository.java
│   │   │   └── ClaimsRepositoryImpl.java
│   │   ├── model/
│   │   │   ├── Claim.java
│   │   │   └── ClaimSummary.java
│   │   └── config/
│   │       └── AwsConfig.java
│   └── resources/
│       ├── application.yml
│       └── logback-spring.xml
└── test/
    ├── java/com/example/claims/
    │   └── ClaimsApplicationTests.java
    └── resources/
        └── application.yml
```

## Deployment

Deploy to Kubernetes using the provided manifests in the `k8s/` directory (to be created).

## Contributing

1. Follow standard Spring Boot project structure
2. Add unit tests for new functionality
3. Update documentation for API changes
4. Ensure all tests pass before committing