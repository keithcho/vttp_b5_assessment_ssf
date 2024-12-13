FROM eclipse-temurin:23-noble AS builder

WORKDIR /src

COPY mvnw .
COPY pom.xml .

COPY .mvn .mvn
COPY src src

RUN chmod a+x mvnw && ./mvnw package -Dmaven.test.skip=true

FROM eclipse-temurin:23-jre-noble

WORKDIR /app

COPY --from=builder src/target/noticeboard-0.0.1-SNAPSHOT.jar app.jar

RUN apt-get update && apt-get install -y curl

ENV PUBLISHING_SERVER_URL=https://publishing-production-d35a.up.railway.app/
ENV PORT=8080

EXPOSE ${PORT}

HEALTHCHECK --interval=60s --timeout=30s --start-period=120s --retries=3 \
    CMD curl -s -f http://localhost:${PORT}/status || exit 1

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar