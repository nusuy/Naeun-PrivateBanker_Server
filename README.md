# Private Banker, Naeun Server

나만의 은행원, 나은 서버 소스코드

## About Naeun

## Tech Architecture

## How to Run on Local

로컬에서 서버 작동시키는 방법

#### 1. Clone this Repository

```bash
git clone https://github.com/Naeun-privatebanker/Server
```

#### 2. Create `application.properties` file on `Naeun_server/src/main/resources`

```properties
spring.application.name=Naeun_server
spring.profiles.active=prod

# JPA
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/naeun
SPRING_DATASOURCE_USERNAME={YOUR_DB_USERNAME}
SPRING_DATASOURCE_PASSWORD={YOUR_DB_PASSWORD}

# Redis
SPRING_DATA_REDIS_HOST=localhost
SPRING_DATA_REDIS_PORT=6379
SPRING_DATA_REDIS_PASSWORD={YOUR_REDIS_PASSWORD}

# JWT
JWT_SECRET_KEY=some_random_secret_key_3654
JWT_ISSUER=naeun

# GCP Bucket
GCP_PROJECT_ID={YOUR_GCP_PROJECT_ID}
GCP_BUCKET={YOUR_GCP_BUCKET_NAME}

# FastAPI
FAST_API_BASE_URL=http://localhost:8000
```

#### 3. Store the gcp bucket service account JSON file `bucket-key.json` on `Naeun_server/src/main/resources`

#### 4. Run the server application
