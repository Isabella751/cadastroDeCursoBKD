# ==============================================
# ESTÁGIO 1: Build (compila o código)
# ==============================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copia arquivos de configuração (aproveita cache do Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests

# ==============================================
# ESTÁGIO 2: Runtime (imagem final LEVE)
# ==============================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o JAR do estágio anterior (é aqui que o --from=builder resolve o problema!)
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
    "-Xmx256m", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=70.0", \
    "-jar", "app.jar"]