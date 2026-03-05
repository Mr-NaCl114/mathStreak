# 基础镜像 (根据 pom.xml 中的 java.version=21 选择 JDK 21 的运行时镜像)
FROM eclipse-temurin:21-jre

# 配置环境变量
ENV PARAMS=""

# 时区设置 (设置为中国标准时间)
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
# 根据 pom.xml，artifactId 为 mathStreak-web
# 这里的 target/mathStreak-web-*.jar 会通配匹配 target 目录下的构建产物 (如 mathStreak-web-1.0.1.jar)
# 将其复制并重命名为 app.jar
ADD target/mathStreak-web-*.jar /app.jar

# 暴露端口 (对应 application-dev.yml 中 server.port: 10001)
EXPOSE 10001

# 启动命令
ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /app.jar $PARAMS"]
