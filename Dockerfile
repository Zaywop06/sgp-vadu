# ======================
# Build stage
# ======================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ======================
# Runtime stage
# ======================
FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache dumb-init

RUN addgroup -g 1000 spring \
 && adduser -u 1000 -G spring -s /bin/sh -D spring

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
RUN chown -R spring:spring /app

USER spring

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["dumb-init", "java", "-Xmx512m", "-jar", "app.jar"]
