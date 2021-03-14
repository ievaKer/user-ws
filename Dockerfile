FROM openjdk:8

ENV HOME /usr/user-ws
ENV APP user-ws.jar

WORKDIR $HOME

COPY target/$APP $HOME