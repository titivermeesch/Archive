FROM gradle:jdk17 AS build

WORKDIR /usr/app/
COPY . .

RUN gradle build

FROM amazoncorretto:17-alpine3.19

ENV JAR_NAME=app.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY --from=BUILD $APP_HOME .

CMD exec java -jar "$APP_HOME"/build/libs/"$JAR_NAME"

EXPOSE 25565