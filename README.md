# CAS 开源登录解决方案
https://github.com/apereo/cas
Enterprise Single Sign On  企业级单点登录开源解决方案

 

![CAS Architecture](https://apereo.github.io/cas/5.2.x/images/cas_architecture.png)

CAS Architecture Diagram
# Spring Cloud

![Spring Cloud](https://spring.io/img/homepage/diagram-distributed-systems.svg)

# JHipster
基于Spring Boot Angular4 的脚手架
To install Yeoman, type: npm install -g yo 
To install JHipster, type: npm install -g generator-jhipster

jhipster 
jhipster entity author 
jhipster entity book







# 应用模块
| 模块名 |模块介绍|端口情况|必须https|path|启动循序
|:-------|:-------|:----|:-------|:-----|:--|
|doit-sso-server|cas服务|8443|√|cas|2|
|doit-sso-config|配置中心|8888|√|config|1|
|doit-sso-management|service管理|8081|√|cas-management|3|
|doit-api-gateway|cloud 网关|8888|√|/doit-api-gateway|4|
|doit-eureka-server|cloud 服务治理中心|1111|√|/|5|
|doit-hystrix-dashboard|cloud 熔断dashboard|2001|×|/|6|
|doit-zipkin-server|cloud trace监控|9411|×|/|7|
|doit-blog|前后分离微服务实例|8080|×|/|8|
|doit-shiro-client|shiro集成cas实例|8083|×|/|9|



