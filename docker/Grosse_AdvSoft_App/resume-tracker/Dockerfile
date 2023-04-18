FROM eclipse-temurin:17.0.6_10-jre-alpine

# copy content to image
RUN mkdir /app
WORKDIR /app

RUN wget https://github.com/jgrosse01/CS389_AdvSoftwareProject/releases/download/v0.0.3-alpha/cs389-0.0.3-alpha.jar

EXPOSE 80

COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]

CMD ["java", "-jar", "-Dserver.port=80",  "cs389-0.0.3-alpha.jar"]
