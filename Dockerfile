# First Stage - Build
FROM maven:3.9.9-eclipse-temurin-23 AS builder
ARG COMPILE_DIR=/compiledir
WORKDIR ${COMPILE_DIR}

# Copy necessary files for Maven
COPY mvnw . 
COPY mvnw.cmd . 
COPY pom.xml . 
COPY .mvn .mvn 
COPY src src  

# Set executable permission for mvnw
RUN chmod +x mvnw

# Run Maven to build the project
RUN ./mvnw clean package -DskipTests

# Second Stage - Runtime
FROM maven:3.9.9-eclipse-temurin-23
ARG WORK_DIR=/app
WORKDIR ${WORK_DIR}

# Copy the application jar from the build stage
COPY --from=builder /compiledir/target/noticeboard-0.0.1-SNAPSHOT.jar noticeboardapp.jar

# Set environment variables
ENV SERVER_PORT=8080

# Expose port
EXPOSE ${SERVER_PORT}

# Command to run the application
ENTRYPOINT ["java", "-jar", "noticeboardapp.jar"]

# Build and run the container
# docker build -t cihansifan/vttp5b-ssf-day17l:v0.0.1 .  
# docker run -p 8085:8080 cihansifan/vttp5b-ssf-day17l:v0.0.1