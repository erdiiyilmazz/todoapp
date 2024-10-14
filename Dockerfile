FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/*.jar app.jar

COPY wait-for-couchbase.sh /wait-for-couchbase.sh

RUN chmod +x /wait-for-couchbase.sh

EXPOSE 8080

CMD ["/wait-for-couchbase.sh", "couchbase", "java", "-jar", "app.jar"]
