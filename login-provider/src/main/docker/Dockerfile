FROM 10.3.98.241/library/java:8-alpine
MAINTAINER xuyf
ENV TIME_ZONE Asia/Shanghai
RUN apk add --no-cache tzdata && echo "${TIME_ZONE}" > /etc/timezone && ln -sf /usr/share/zoneinfo/${TIME_ZONE} /etc/localtime
VOLUME /sceo-log/log
ADD *.jar app.jar
EXPOSE 9010
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.cloud.config.profile=show"]