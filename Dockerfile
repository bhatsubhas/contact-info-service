FROM openjdk:17-jdk-slim-bullseye AS build-env
COPY . /app
WORKDIR /app
RUN ./gradlew build

FROM gcr.io/distroless/java17-debian12:nonroot
COPY --from=build-env /app/build/libs/contact-info-service-0.0.1-SNAPSHOT.jar /app/contact-service.jar
WORKDIR /app
CMD [ "contact-service.jar" ]
