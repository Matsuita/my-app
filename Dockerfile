FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# 👇追加（超重要）
RUN ls -R

CMD sh -c "java -jar target/*.jar"
