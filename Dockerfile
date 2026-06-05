FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /workspace

ARG GITHUB_ACTOR
ARG GITHUB_TOKEN

RUN mkdir -p /root/.m2 && cat > /root/.m2/settings.xml <<EOF
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
	<servers>
		<server>
			<id>github</id>
			<username>${GITHUB_ACTOR}</username>
			<password>${GITHUB_TOKEN}</password>
		</server>
	</servers>
</settings>
EOF

COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /workspace/target/*.jar app.jar

USER spring

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]