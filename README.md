# 基于unidbg0.9.4和spring boot 2.5.3开发的高并发server服务器

## application.yml 讲解

```yaml
server:
  # 端口
  port: 9999

application:
  unidbg:
    # 是否启用 dynarmic 引擎
    dynarmic: false
    # 是否打印jni调用细节 vm.setVerbose()
    verbose: false

# 多线程相关
spring:
  task:
    execution:
      pool:
        allow-core-thread-timeout: true
        # 8个核心线程
        core-size: 8
        # 超过多久没用的线程自动释放
        keep-alive: 60s
        # 最多增长到多少线程
        max-size: 8
```

## 使用

### 环境准备

1. 必须使用Java8
2. Maven3.5以上 ，如果电脑没有安装Maven，最简单办法是将下面的 `mvn` 命令替换成 `mvnw` ，会自动下载maven 

### 使用定制化/快照版unidbg

```bash
git clone https://github.com/zhkl0228/unidbg.git
cd unidbg
mvn clean install -Dgpg.skip=true -T10
```

以最新快照版 `0.9.5-SNAPSHOT` 为例，修改 `unidbg-boot-server/pom.xml` 里的 `<unidbg.version>0.9.4</unidbg.version>`
为 `<unidbg.version>0.9.5-SNAPSHOT</unidbg.version>`

后续java打包或者docker不变

### java 打包

```
# 打包
mvn package -T10 -DskipTests
# 运行
java -jar target\unidbg-boot-server-0.0.1-SNAPSHOT.jar
```

### docker打包

用docker打包是为了避免个人电脑和生产服务器环境不一致导致的启动失败或者各种问题，保证了开发和生产环境的一致性，以及快速安装等需求

如何安装docker 参考docker官方文档 https://docs.docker.com/engine/install/

**注意**

- 如果是windows的powershell, - 需要改成 `- ,建议windows用cmd
- 将 your_docker_hub_username 换成真实的用户名 ,将 your_docker_hub_password 换成真实的密码

```bash

# 方案1 打包并发布到docker hub
mvn compile -Djib.to.auth.username=your_docker_hub_username  -Djib.to.auth.password=your_docker_hub_password -Djib.to.image=your_docker_hub_username/unidbg-boot-server  jib:build -Dmaven.test.skip=true --batch-mode -T4

# 方案2 直接打到docker 守护进程里
mvn compile  -Djib.to.image=your_docker_hub_password/unidbg-boot-server  jib:dockerBuild -Dmaven.test.skip=true --batch-mode -T4

# 方案3 打成docker.tar二进制包
mvn compile  -Djib.to.image=your_docker_hub_password/unidbg-boot-server  jib:buildTar -Dmaven.test.skip=true --batch-mode -T4
docker load --input target/jib-image.tar

# 在装有docker的机器上运行
sudo docker run  -d -p9999:9999 your_docker_hub_password/unidbg-boot-server 

```

## 快速体验

```bash
# 体验jar版本
mvn package -T10 -DskipTests
java -jar target\unidbg-boot-server-0.0.1-SNAPSHOT.jar

# 体验docker版本
docker run --restart=always -d -p9999:9999 anjia0532/unidbg-boot-server 
```

## 调用

```bash
curl  http://127.0.0.1:9999/api/tt-encrypt/encrypt
```

## 压测

在我个人开发电脑上(`Intel(R) Core(TM) i7-9750H CPU @ 2.60GHz   2.60 GHz`,`32.0 GB (31.9 GB 可用)`,`win 11 64位`)，压测结果是每秒4003.10次(QPS 4003.10) ，wrk相关教程，可以参考 [HTTP压测工具之wrk](https://www.jianshu.com/p/ac185e01cc30)

```
[root@wrk]# docker run --rm  williamyeh/wrk -t12 -c400 -d30s http://127.0.0.1:9999/api/tt-encrypt/encrypt
Running 30s test @ http://127.0.0.1:9999/api/tt-encrypt/encrypt
  12 threads and 400 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   105.55ms   68.17ms 982.93ms   94.97%
    Req/Sec   341.43     55.05   460.00     80.70%
  120432 requests in 30.08s, 14.72MB read
  Socket errors: connect 0, read 0, write 81, timeout 0
Requests/sec:   4003.10
Transfer/sec:    501.09KB
```

瓶颈在cpu上,demo内存基本在400-600M左右，不会随着并发高而暴涨(注意，仅是此demo情况下，具体还是看实际业务复杂度)

![](docs/1.png)

## [常见问题](QA.md)

参见  [QA.md](QA.md)

## 请我喝杯咖啡

如果觉得本项目对你有所帮助，可以请我喝杯咖啡吗？

<table>
    <tr>
        <td ><center><img src="./docs/wechat.png" >微&nbsp;&nbsp;信</center></td>
        <td ><center><img src="./docs/alipay.jpg" >支付宝</center></td>
    </tr>
</table>

![](./docs/zsxq.png)