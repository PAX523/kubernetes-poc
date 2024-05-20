FROM eclipse-temurin:21-jre-alpine

# specify environment variables
ARG MYSQL_HOST=mysql-server
ENV MYSQL_HOST=$MYSQL_HOST

ARG MYSQL_PORT=3306
ENV MYSQL_PORT=$MYSQL_PORT

ARG SERVER_PORT=8081
ENV SERVER_PORT=$SERVER_PORT

# setup
COPY docker/run.sh /opt/service/
COPY target/internal-service-1.0.0-SNAPSHOT.jar /opt/service/service.jar
RUN chmod +x /opt/service/run.sh && \
    chmod +x /opt/service/service.jar

# run
ENTRYPOINT ["/opt/service/run.sh"]
