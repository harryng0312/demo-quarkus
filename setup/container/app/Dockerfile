FROM openjdk:latest
ENV DEMO_HOME /opt/demo-communication
WORKDIR $DEMO_HOME
#ADD ./demo-communication-web/build/libs/* $DEMO_HOME/
COPY ./demo-communication-web/build/libs $DEMO_HOME
EXPOSE 9090
CMD java -jar $DEMO_HOME/demo-communication-web-1.0-SNAPSHOT.war