FROM eclipse-temurin:21-jre-alpine

# specify environment variables
ENV MYSQL_HOST=mysql-server
ENV MYSQL_PORT=3306
ENV SERVER_PORT=8081

# setup
COPY docker/run.sh /opt/service/
COPY target/internal-service-1.0.0-SNAPSHOT.jar /opt/service/service.jar
RUN chmod +x /opt/service/run.sh && \
    chmod +x /opt/service/service.jar

# run
ENTRYPOINT ["/opt/service/run.sh"]
