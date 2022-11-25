FROM openjdk:17-alpine as build
WORKDIR /build
COPY .mvn .mvn
COPY pom.xml mvnw ./
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw clean package

FROM openjdk:17-alpine
WORKDIR /backend
COPY --from=build /build/target/test*.jar ./backend.jar

EXPOSE 8080
CMD java -jar /backend/backend.jar