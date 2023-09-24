FROM openjdk:20
ARG JAR_FILE
COPY target/shop-0.0.1-SNAPSHOT.jar /shop-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/shop-0.0.1-SNAPSHOT.jar","-web -webAllowOthers -tcp -tcpAllowOthers -browser"]