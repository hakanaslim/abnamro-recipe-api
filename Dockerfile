FROM eclipse-temurin:17-jre-alpine
RUN addgroup --system javauser && \
    adduser -S -s /bin/false -G javauser javauser && \
    mkdir /app && \
    chown -R javauser:javauser /app
ADD --chown=javauser:javauser target/*.jar /app/app.jar
WORKDIR /app
ENV SERVER_PORT=8080 \
    JVM_OPTS="-server -XX:InitialRAMPercentage=30.0 -XX:MinRAMPercentage=30.0 -XX:MaxRAMPercentage=70.0 -Dserver.port=8080"
USER javauser
EXPOSE $SERVER_PORT
CMD java $JVM_OPTS -jar /app/app.jar
