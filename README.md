# app-console
一款Spring Boot应用治理框架

# build 
````shell script
mvn package  
````
将会在console-dist/target下生成一个app-console-bin.tar.gz的文件,压缩包结构如下

````
|____server
| |____app-console-server.jar           管控端程序入口
| |____application.yml                  管控端配置文件
|____agent
| |____config
| | |____config.properties              agent配置文件
| |____extensions                       agent扩展包
| | |____spring-register.jar
| | |____spring-ratelimit.jar
| | |____console-boot.jar
| | |____console-common-1.0.0.jar
| |____console-agent.jar                agent
````

