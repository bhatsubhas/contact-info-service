.PHONY: build
SHELL := /bin/bash
image_name = contact-service
image_tag = 1.0.0

build: Dockerfile
	docker image build -t $(image_name):$(image_tag) .

analyze:
	dive $(image_name):$(image_tag)

scan:
	trivy image --exit-code 1 --severity CRITICAL,HIGH $(image_name):$(image_tag)

run:
	docker container run -d --rm --name $(image_name) -p 8080:8080 $(image_name):$(image_tag)

logs:
	docker container logs -f $(image_name)

stop:
	docker container stop $(image_name)

prune:
	docker image prune -f
