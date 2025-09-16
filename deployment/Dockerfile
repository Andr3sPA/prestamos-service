FROM eclipse-temurin:21-jdk-alpine

# Crear usuario seguro
RUN adduser -D appuser

VOLUME /tmp

# Copiar el JAR generado desde la ruta correcta
COPY applications/app-service/build/libs/JavaReactivo.jar /app/JavaReactivo.jar

WORKDIR /app

# Opciones compatibles con HotSpot
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8081

USER appuser

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar JavaReactivo.jar"]
