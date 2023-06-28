# 基础镜像
FROM yingrui/openjdk1.8
# 设置工作目录
WORKDIR /app
# 将应用程序的JAR文件复制到容器中
COPY target/receivingCode-0.0.1-SNAPSHOT.jar app.jar
# 暴露端口
EXPOSE 8090
# 执行启动命令
CMD ["java", "-jar", "app.jar"]
