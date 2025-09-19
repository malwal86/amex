# build
FROM gradle:8.10-jdk17 AS build
WORKDIR /workspace
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle --no-daemon clean installDist

# run
FROM eclipse-temurin:17-jre
ENV PORT=8888
WORKDIR /app
COPY --from=build /workspace/build/install/amex-assessment/ ./
EXPOSE 8888
CMD ["./bin/amex-assessment"]