# LinkedIn Microservices Project (Dockerized)

This repository contains a **Dockerized microservices-based LinkedIn-like backend system**. All microservices are published as Docker images and can be pulled and run easily using **Docker Compose**.

---

## 🧱 Architecture Overview

The project follows a **microservices architecture** and includes the following services:

- API Gateway
- Discovery Service (Eureka)
- User Service
- Connections Service
- Post Service
- Notification Service

All services are containerized and hosted in **Docker Hub under a single repository using tags**.

Docker Hub Repository:

```
rohanpokale/linkedin_project_images
```

---

## 📦 Prerequisites

Make sure you have the following installed:

- Docker (v20+ recommended)
- Docker Compose (v2+ recommended)

Verify installation:

```bash
docker --version
docker compose version
```

---

## 🔐 Step 1: Login to Docker Hub

```bash
docker login
```

Enter your Docker Hub credentials when prompted.

---

## ⬇️ Step 2: Pull All Microservice Images

Each microservice is stored as a **tag** under a single Docker Hub repository.

```bash
docker pull rohanpokale/linkedin_project_images:api-gateway
docker pull rohanpokale/linkedin_project_images:discovery-service
docker pull rohanpokale/linkedin_project_images:user-service
docker pull rohanpokale/linkedin_project_images:connections-service
docker pull rohanpokale/linkedin_project_images:post-service
docker pull rohanpokale/linkedin_project_images:notification-service
```

Verify pulled images:

```bash
docker images
```

---

## 🧩 Step 3: Docker Compose File

Create a file named `docker-compose.yml` in the root directory.

```yaml
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.5.0
    container_name: zookeeper
    ports:
      - "2181:2181"
    networks:
      - linkedin-network
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:7.5.0
    container_name: kafka
    ports:
      - "9092:9092"
    networks:
      - linkedin-network
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_LOG_DIRS: /var/lib/kafka/data
    volumes:
      - kafka_data:/var/lib/kafka/data
    depends_on:
      - zookeeper


  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    container_name: kafka-ui
    ports:
      - "8080:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9092
      KAFKA_CLUSTERS_0_ZOOKEEPER: zookeeper:2181
    depends_on:
      - kafka
      - zookeeper

  notification-db:
    image: postgres:15
    container_name: notification-db
    environment:
      POSTGRES_DB: LinkedInNotificationService
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: Root@123
    volumes:
      - notification_data:/var/lib/postgresql/data
    networks:
      - linkedin-network

  posts-db:
    image: postgres:15
    container_name: post-db
    environment:
      POSTGRES_DB: LinkedInPostService
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: Root@123
    volumes:
      - post_data:/var/lib/postgresql/data
    networks:
      - linkedin-network

  user-db:
    image: postgres:15
    container_name: user-db
    environment:
      POSTGRES_DB: LinkedInUserService
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: Root@123
    volumes:
      - user_data:/var/lib/postgresql/data
    networks:
      - linkedin-network

  connections-db:
    image: neo4j:5
    container_name: connections-db
    ports:
      - "7474:7474"   # Neo4j Browser (HTTP)
      - "7687:7687"   # Bolt protocol
    environment:
      NEO4J_AUTH: neo4j/Root@123
      NEO4J_dbms_default__database: linkedindb
    volumes:
      - connections_data:/data
    networks:
      - linkedin-network

  discovery-service:
    image: rohanpokale/linked-in/discovery-service:0.0.1
    container_name: discovery-service
    networks:
      - linkedin-network
    ports:
      - "8761:8761"

  post-service:
    image: rohanpokale/linked-in/post-service:0.0.1
    container_name: post-service
    networks:
      - linkedin-network
    depends_on:
      - discovery-service
      - posts-db
      - kafka

  user-service:
    image: rohanpokale/linked-in/user-service:0.0.1
    container_name: user-service
    networks:
      - linkedin-network
    depends_on:
      - discovery-service
      - user-db
      - kafka

  notification-service:
    image: rohanpokale/linked-in/notification-service:0.0.1
    container_name: notification-service
    networks:
      - linkedin-network
    depends_on:
      - discovery-service
      - notification-db
      - kafka

  connections-service:
    image: rohanpokale/linked-in/connections-service:0.0.1
    container_name: connections-service
    networks:
      - linkedin-network
    depends_on:
      - discovery-service
      - connections-db
      - kafka

  api-gateway:
    image: rohanpokale/linked-in/api-gateway:0.0.1
    container_name: api-gateway
    ports:
      - "8090:8090"
    networks:
      - linkedin-network
    depends_on:
      - discovery-service

volumes:
  notification_data:
  kafka_data:
  post_data:
  user_data:
  connections_data:

networks:
  linkedin-network:
```

---

## ▶️ Step 4: Run the Entire Project

From the directory containing `docker-compose.yml`:

```bash
docker compose up -d
```

To check running containers:

```bash
docker ps
```

---

## ⏹️ Stop the Project

```bash
docker compose down
```

---

## 🌐 Access Services

| Service          | URL                                            |
| ---------------- | ---------------------------------------------- |
| API Gateway      | [http://localhost:8080](http://localhost:8080) |
| Eureka Dashboard | [http://localhost:8761](http://localhost:8761) |

---

## 🧹 Cleanup (Optional)

Remove unused images and containers:

```bash
docker system prune -a
```

---

## 🧠 Important Notes

- All microservices are stored in **one Docker Hub repository using tags**.
- Docker Hub does **not support nested repositories**.
- Re-tagging images does **not consume additional disk space**.
- This setup is ideal for **local development and learning purposes**.

---

## 📌 Author

**Rohan Pokale**\
Dockerized Microservices Project

---

⭐ If you find this project useful, feel free to star the repository!

