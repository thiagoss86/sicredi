FROM openjdk
MAINTAINER github/thiagoss86
WORKDIR /app
COPY target/sicredi-0.0.1-SNAPSHOT.jar /app/sicredi.jar
ENTRYPOINT ["java", "-jar", "sicredi.jar"]
EXPOSE 8080