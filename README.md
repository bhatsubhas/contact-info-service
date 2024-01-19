# Contact-Info-Service
A microservice to store and manage contact details.
## Requirements:
* Java 17
* Docker
* make (optional)
## Execute unit tests and see the coverage report
Run the tests using `./gradlew test` command and then fro cod coverage report open **build/reports/jacoco/test/html/index.html** in a browser.
## Start the application and access API Documenation
Run `./gradlew bootRun` command and access API Documentation here: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
## Helpful gradle commands
To run the unit tests locally run:
```bash
./gradlew test
```
To build the jar locally and start the application from jar run:
```bash
./gradlew build -x test
java -jar build/libs/contact-info-service-0.0.1-SNAPSHOT.jar
```
To run the application locally on port `8080` run:
```bash
./gradlew bootRun
```
Top clean the local gradle build directory run:
```bash
./gradlew clean
```
## Helpful *make* targets (Optional)
To build the docker image run:
```bash
make image
```
To analyze image size using **dive** tool the docker image run:
```bash
make analyze
```
To scan the image for vulnerabilities using **trivy** tool run:
```bash
make scan
```
To run the docker container locally in port `8080` run:
```bash
make run
```
To see the logs of the running container run:
```bash
make logs
```
To stop the running container run:
```bash
make stop
```
Top clean the local gradle build directory and local docker image run:
```bash
make purge
