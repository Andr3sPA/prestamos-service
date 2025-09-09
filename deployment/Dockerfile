# Usa una imagen ligera con JDK 21
FROM eclipse-temurin:21-jdk-alpine

VOLUME /tmp

# Copia el JAR ejecutable del proyecto de préstamos
COPY applications/app-service/build/libs/JavaReactivo.jar app.jar

ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
EXPOSE 8081
