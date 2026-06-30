FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -B --no-transfer-progress

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN mkdir -p /app/data
VOLUME ["/app/data"]
ENV INPUT_PATH=/app/data/input.json
ENV OUTPUT_PATH=/app/data/output.json
ENTRYPOINT ["java", "-jar", "app.jar"]
