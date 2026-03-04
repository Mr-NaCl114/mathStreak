# -------------------------------------------------------------------
# 第一阶段：构建 (Build Stage)
# 使用包含 Maven 和 JDK 21 的官方镜像
# -------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS builder

# 设置工作目录
WORKDIR /app

# 将项目所有文件复制到镜像中
# 注意：这里假设 .dockerignore 已经排除了 target、.idea 等不必要文件
COPY . .

# 执行 Maven 打包
# -pl mathStreak-web: 指定构建 web 模块
# -am: 同时构建该模块所依赖的其他模块 (domain, service, common 等)
# -DskipTests: 跳过测试以加快构建速度
RUN mvn clean package -DskipTests -pl mathStreak-web -am

# -------------------------------------------------------------------
# 第二阶段：运行时 (Runtime Stage)
# 仅包含 JRE，体积更小
# -------------------------------------------------------------------
FROM eclipse-temurin:21-jre

# 设置工作目录
WORKDIR /app

# 定义环境变量，默认使用 dev 环境（对应 application-dev.yml）
ENV SPRING_PROFILES_ACTIVE=dev
ENV JAVA_OPTS=""

# 从第一阶段复制构建好的 Jar 包
# 这里假设生成的 jar 包位于 mathStreak-web/target/ 目录下
# 具体的 jar 名称可能因 pom 中 finalName 的配置而异，这里使用通配符适配
COPY --from=builder /app/mathStreak-web/target/*.jar app.jar

# 暴露端口 (对应 application-dev.yml 中的 server.port)
EXPOSE 10001

# 启动命令
# 使用 exec 格式以确保信号能够正确传递给 JVM
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
