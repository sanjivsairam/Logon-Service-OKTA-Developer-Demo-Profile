FROM openjdk:15-alpine
ENV context ""
ARG STAGEENV
ENV PLATFORM=$STAGEENV
RUN echo ${PLATFORM}
RUN echo ${STAGEENV}
ADD /src/main/resources/application-${STAGEENV}.properties //
ADD /target/simulation-login-service-0.0.1-SNAPSHOT.jar //
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=${PLATFORM}", "/simulation-login-service-0.0.1-SNAPSHOT.jar", "--server.servlet.context-path=${context}"]
