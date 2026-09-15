.PHONY: db-up db-down db-logs db-status test test-it

COMPOSE_FILE := infrastructure/docker/docker-compose.yml

db-up:
	docker compose -f $(COMPOSE_FILE) up -d

db-down:
	docker compose -f $(COMPOSE_FILE) down

db-logs:
	docker compose -f $(COMPOSE_FILE) logs -f postgres

db-status:
	docker compose -f $(COMPOSE_FILE) ps

test:
	./mvnw test

test-it:
	./mvnw verify
