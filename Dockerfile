FROM arm64v8/eclipse-temurin:17-jre
MAINTAINER serejka
EXPOSE 5555
COPY target/behold-0.0.1-SNAPSHOT.jar behold-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/behold-0.0.1-SNAPSHOT.jar"]
