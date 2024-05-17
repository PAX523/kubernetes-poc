FROM eclipse-temurin:21-jre-alpine

# specify environment variables
ARG MYSQL_HOST=localhost
ENV MYSQL_HOST=$MYSQL_HOST

ARG MYSQL_PORT=3306
ENV MYSQL_PORT=$MYSQL_PORT

ARG MYSQL_USER=root
ENV MYSQL_USER=$MYSQL_USER

ARG MYSQL_PASSWORD=root
ENV MYSQL_PASSWORD=$MYSQL_PASSWORD

# setup
COPY run.sh /opt/service/
COPY ../../../target/internal-service-1.0.0-SNAPSHOT.jar /opt/service/service.jar
RUN chmod +x /opt/service/run.sh && \
    chmod +x /opt/service/service.jar

# run
ENTRYPOINT ["/opt/service/run.sh"]
