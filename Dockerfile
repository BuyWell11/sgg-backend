# Используем официальный образ Maven с OpenJDK
FROM maven:3.8.6 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл с зависимостями
COPY pom.xml ./
COPY src ./src

# Собираем проект
RUN mvn clean package -DskipTests

# Используем официальный образ Java для финального образа
FROM openjdk:17-jdk-slim

# Копируем скомпилированный jar файл в контейнер
COPY --from=build /app/target/*.jar app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
