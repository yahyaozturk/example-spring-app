FROM openjdk:8-jre

ENTRYPOINT ["/usr/bin/java", "-jar", "/opt/myservice.jar"]

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /opt/myservice.jar
