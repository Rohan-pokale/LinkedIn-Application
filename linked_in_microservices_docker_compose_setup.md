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
version: '3.8'

services:
  discovery-service:
    image: rohanpokale/linkedin_project_images:discovery-service
    container_name: discovery-service
    ports:
      - "8761:8761"

  api-gateway:
    image: rohanpokale/linkedin_project_images:api-gateway
    container_name: api-gateway
    ports:
      - "8080:8080"
    depends_on:
      - discovery-service

  user-service:
    image: rohanpokale/linkedin_project_images:user-service
    container_name: user-service
    depends_on:
      - discovery-service

  connections-service:
    image: rohanpokale/linkedin_project_images:connections-service
    container_name: connections-service
    depends_on:
      - discovery-service

  post-service:
    image: rohanpokale/linkedin_project_images:post-service
    container_name: post-service
    depends_on:
      - discovery-service

  notification-service:
    image: rohanpokale/linkedin_project_images:notification-service
    container_name: notification-service
    depends_on:
      - discovery-service
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

