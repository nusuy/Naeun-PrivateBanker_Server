# Private Banker, Naeun Server

나만의 은행원, 나은 서버 소스코드

## About Naeun

Naeun is an AI banker service to prevent the elderly from being victimized by mis-selling and help them respond after
the fact.<br>
To prevent financial damage to the elderly due to mis-selling, the service strengthens the crisis management
capabilities of elderly consumers through financial technology specialized for the elderly.
<br><br>
나은(Naeun)은 불완전판매로 인한 고령층의 피해를 사전 방지하고 사후 대응을 돕기 위한 AI 은행원 서비스입니다. <br>
불완전판매로 인한 고령층의 금융피해를 방지하기 위해 노인 전용 금융 기술을 통해 고령소비자의 위기관리 능력을 강화합니다.

## Tech Architecture

![Tech Stack](https://github.com/Naeun-privatebanker/Server/assets/68212300/d4f996a9-cabb-43a2-a1a3-c5985ea5b614)

## Fast API Repository Link

Gemini 프롬프트 답변을 전달하기 위한 Fast API 소스코드 Repo 입니다. <br>
[Repository Link](https://github.com/nusuy/Naeun-PrivateBanker_FastAPI)

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
