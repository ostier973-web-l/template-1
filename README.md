# Spring Boot CRUD with MongoDB and Redis

This project provides a simple REST API for managing `Item` entities. Data is persisted in a sharded MongoDB cluster and cached in a sharded Redis cluster. It also demonstrates calling an external service via OpenFeign.

## Running locally

Start required infrastructure using Docker Compose:

```bash
# MongoDB cluster
docker-compose -f docker-compose.mongo.yml up -d

# Redis cluster
# After containers start, create the cluster:
# docker exec -it redis1 redis-cli --cluster create redis1:6379 redis2:6379 redis3:6379 --cluster-replicas 0

docker-compose -f docker-compose.redis.yml up -d
```

Then run the application:

```bash
mvn spring-boot:run
```

The API will be available on `http://localhost:8080/items`.

To test the external call, send a GET request to `http://localhost:8080/items/external/status`. The base URL of the target service is configured through `external.service.url` in `application.yml`.
