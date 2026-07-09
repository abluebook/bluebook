FROM maven:3.9-eclipse-temurin-17 AS mvn_build

WORKDIR /opt/bluebook/
ADD . /tmp
RUN cd /tmp && mvn package -DskipTests -Pci -q && mv target/symphony-bin/* /opt/bluebook/ \
&& cp -f /tmp/src/main/resources/docker/* /opt/bluebook/

FROM eclipse-temurin:18-jre-alpine
LABEL maintainer="starsers <yujieweb@foxmail.com>"

WORKDIR /opt/bluebook/
COPY --from=mvn_build /opt/bluebook/ /opt/bluebook/
RUN apk add --no-cache ca-certificates tzdata ttf-dejavu

ENV TZ=Asia/Shanghai
EXPOSE 8080

ENTRYPOINT [ "java", "-cp", "lib/*:.", "org.b3log.symphony.Server" ]
