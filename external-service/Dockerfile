FROM eclipse-temurin:21-jre-alpine

# specify environment variables
ENV INTERNAL_SERVER_HOST=internal-service
ENV INTERNAL_SERVER_PORT=8081
ENV SERVER_PORT=8080

# setup
COPY docker/run.sh /opt/service/
COPY target/external-service-1.0.0-SNAPSHOT.jar /opt/service/service.jar
RUN chmod +x /opt/service/run.sh && \
    chmod +x /opt/service/service.jar

# run
ENTRYPOINT ["/opt/service/run.sh"]
