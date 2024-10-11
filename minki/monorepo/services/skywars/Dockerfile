#FROM gradle:8.7.0-jdk17-alpine as build
#
#WORKDIR /app
#COPY . .
#
#ARG PORT
#
#RUN ./gradlew shadowJar

FROM amazoncorretto:21-alpine3.19

WORKDIR /runtime

COPY build/libs/*.jar app.jar
#COPY --from=build /app/build/libs/*.jar app.jar

ENV PORT=25565

EXPOSE 25565
CMD ["java", "-jar", "app.jar"]

