FROM eclipse-temurin:21-jre-alpine

# specify environment variables
ARG INTERNAL_SERVER_HOST=internal-service
ENV INTERNAL_SERVER_HOST=$INTERNAL_SERVER_HOST

ARG INTERNAL_SERVER_PORT=8081
ENV INTERNAL_SERVER_PORT=$INTERNAL_SERVER_PORT

ARG SERVER_PORT=8080
ENV SERVER_PORT=$SERVER_PORT

# setup
COPY docker/run.sh /opt/service/
COPY target/external-service-1.0.0-SNAPSHOT.jar /opt/service/service.jar
RUN chmod +x /opt/service/run.sh && \
    chmod +x /opt/service/service.jar

# run
ENTRYPOINT ["/opt/service/run.sh"]
