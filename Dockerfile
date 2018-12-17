FROM openjdk:8-jre
MAINTAINER Michael Smith Jr <mj3397@gmail.com>

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/stl-cbc/stl-cbc.jar"]

ARG JAR_FILE
ADD ./target/${JAR_FILE} /usr/share/stl-cbc/stl-cbc.jar