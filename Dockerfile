FROM ibm-semeru-runtimes:open-17-jre-focal
COPY build/libs/itswednesdaymydudes-0.0.1-SNAPSHOT.jar app.jar
ENV _JAVA_OPTIONS="-XX:InitialRAMPercentage=20 -XX:MaxRAMPercentage=40 -XX:+UseSerialGC -XX:ReservedCodeCacheSize=120m -XX:-TieredCompilation"
CMD java $_JAVA_OPTIONS -jar app.jar
EXPOSE 8080/tcp
EXPOSE 80
EXPOSE 443