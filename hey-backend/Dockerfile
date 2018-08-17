# base image
FROM java:8

MAINTAINER thanhvo

# preserve Java 8  from the maven install.
#RUN mv /etc/alternatives/java /etc/alternatives/java8
#RUN apt-get update -y && apt-get install maven -y

# Restore Java 8
#RUN mv -f /etc/alternatives/java8 /etc/alternatives/java
#RUN ls -l /usr/bin/java && java -version

# set working directory
RUN mkdir /usr/src/app
WORKDIR /usr/src/app
COPY . /usr/src/app
#RUN mvn package -DskipTests

# start app
CMD ["java","-jar","target/hey-backend.jar"]