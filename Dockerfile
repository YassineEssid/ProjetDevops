FROM openjdk:17-jdk-alpine

ENV NEXUS_USERNAME=admin
ENV NEXUS_PASSWORD=0000

WORKDIR /app

EXPOSE 8089

RUN apk add --no-cache curl \
    && curl -u ${NEXUS_USERNAME}:${NEXUS_PASSWORD} -O http://192.168.50.4:8081/repository/maven-releases/tn/esprit/spring/4TWIN3-gestion-station-ski/1.0/4TWIN3-gestion-station-ski-1.0.jar

ENTRYPOINT ["java", "-jar", "4TWIN3-gestion-station-ski-1.0.jar"]