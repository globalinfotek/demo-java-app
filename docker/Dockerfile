FROM openjdk:8-jdk-alpine

RUN apk add --no-cache bash

EXPOSE 8401

ARG VERSION=unknown
ARG TARGET_HOST=localhost
ARG TARGET_PORT=8401

ENV SERVICE_VERSION=$VERSION
ENV UPDATE_HOST=$TARGET_HOST
ENV UPDATE_PORT=$TARGET_PORT

COPY build/libs/demo-java-app.jar app.jar
COPY docker/entrypoint.sh entrypoint.sh

RUN chmod +rx /*.sh \
&& chmod +rx /*.jar

RUN addgroup -S appuser && adduser -S -G appuser appuser
USER appuser

ENTRYPOINT ["./entrypoint.sh"]

LABEL com.giti.demo.demo-java-app.version=$VERSION