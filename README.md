# Canal-Syner

## 什么是Canal-Syner

Canal-Syner 是一个基于[Canal](https://github.com/alibaba/canal)开发的一套用于数据增量同步的SDK，目前预计支持以下几种同步方式: （**勾选内容为已实现部分**）

- [ ] 同步到mysql
- [ ] 同步到elasticsearch
- [ ] 同步到redis
- [x] 同步到kafka(推荐，可以缓存到消息队列订阅消费，流失处理)

## 使用方法
#### 1.导入maven依赖
```
    <groupId>com.faker.canal</groupId>
    <artifactId>canal-syner</artifactId>
    <version>1.0.0-SNAPSHOT</version>
```
#### 2.获取BaseClient,并启动
```
// Simple单机Canal，集群使用Cluster
// 其余参数分别是host,port,canal instance的名称
    BaseClient baseClient = ClientFactory.getClient(ModeEnum.Simple, "localhost", 11111, "test");
    AbstractProcessor kafkaProcessor = new DefaultKafkaProcessor(config);
        baseClient.setProcessor(kafkaProcessor);
        baseClient.start();
```

#### 3. 代码示例见test包中的例子
- PrintTest
- KafkaTest
- RedisTest
- ElasticSearchTest
- MysqlTest


## 代码结构

## 扩展入口
| 类名       | 作用   |  扩展方法  |
| --------   | -----:  | :----:  |
| AbstractProcessor     | 实现了改动数据的概要和内容打印 |   实现process开头的相关方法，完成对数据变动的同步    |


作者: [@wangzhipeng](https://github.com/caicai457614705)  
个人博客: [汪星人的博客](https://caicai457614705.github.io/)
知乎: [王豆豆](https://www.zhihu.com/people/wang-dou-dou-81-79)

2018 年 02月 09日 

