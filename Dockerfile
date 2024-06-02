# Utiliza una imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR generado en el directorio de trabajo del contenedor
COPY target/proyectotest-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que la aplicación Spring Boot se ejecutará
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]