# syntax=docker/dockerfile:1.6
# ============================================================
# Stage 1: Build the WAR with Maven
# ============================================================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# 依存だけ先にダウンロードしてキャッシュさせる
COPY pom.xml ./
RUN mvn -B -ntp dependency:go-offline

COPY src ./src
RUN mvn -B -ntp clean package -DskipTests

# ============================================================
# Stage 2: Run on Tomcat 10.1 / JDK 17
# ============================================================
FROM tomcat:10.1-jdk17-temurin

# Cloud Run は PORT 環境変数を渡す（デフォルト 8080）。
# Tomcat 起動時に server.xml の Connector ポートを書き換える。
ENV PORT=8080

# デフォルトの ROOT アプリを削除してから自分の WAR を ROOT として配置
RUN rm -rf /usr/local/tomcat/webapps/ROOT \
  && rm -rf /usr/local/tomcat/webapps/ROOT.war
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

# 起動時に PORT を Connector に反映してから Tomcat を起動
RUN echo '#!/bin/sh\nset -e\nsed -i "s/port=\"8080\"/port=\"${PORT}\"/" /usr/local/tomcat/conf/server.xml\nexec catalina.sh run' > /usr/local/bin/start.sh \
  && chmod +x /usr/local/bin/start.sh

# JVM チューニング: Cloud Run の起動時間短縮 + コンテナ awareness
ENV CATALINA_OPTS="-Djava.security.egd=file:/dev/./urandom -XX:MaxRAMPercentage=70 -XX:+UseContainerSupport"

EXPOSE 8080
CMD ["/usr/local/bin/start.sh"]
