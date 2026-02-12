# Imagen base con Java 17
FROM eclipse-temurin:17-jdk-alpine

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el jar generado por Maven
COPY target/taskflow-0.0.1-SNAPSHOT.jar app.jar

# Puerto que expone la aplicación
EXPOSE 8080

# Comando que arranca la app
ENTRYPOINT ["java", "-jar", "app.jar"]
