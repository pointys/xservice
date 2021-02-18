git提交from local

branch code

logback-spring.xml、yml配置相关路径
静态资源路径：D:/workspace/static/
rabbitmq：
    交换机 路由 队列
1.先声明交换机、队列、交换机和队列通过路由key的绑定关系
2.生产方通过交换机和路由key发送消息，由第一步的绑定关系向队列推送消息
3.消费方监听队列，获取消息
***生产方如果不知道消费方的队列名称，第一步的配置可以在消费方配置，先启动消费方再启动生产方此时绑定关系已存在
生产方发生消息：rabbitTemplate.convertAndSend(exchange,routing-key,msg);发往绑定关系的队列，此时消费方监听队列即可。

客户端负载均衡：
 ribbon：在客户端启动类加注解：@LoadBalanced//开启客户端负载均衡，然后客户端拿到请求url执行http请求服务端前，会根据url中的服务名进行负载算法轮询各个服务器
 feign:集成ribbon与http两部分，在接口上注解@FeignClient(value="服务名")再接口中的请求方法上的@get、postMapping等注解执行http请求（有参数的@pathavaviable或者@requestparam
 要加参数名）,最后在客户端启动类开启@EnableFeignClients注解（扫描所有@FeignClient接口，并将其接口注入spring容器）
 
页面预览静态化：
    数据模板目前存在mongodb中，但是数据模型存在两个保存地址，前端静态页面数据存在mongodb中（cms_config），课程详情数据存在mysql中（course_base）



生产环境创建索引与导入文档：
1.ruby环境、mysql版本驱动
2.写模板引擎（索引）、logstash执行脚本根据时间戳查询course_pub记录(建议改成项目中的定时器去执行，第三方脚本增加复杂度)
3.遇到的问题：可以创建索引并连接数据库但是向es导入数据报error级别错误，异常为ElasticsearchStatusException[Elasticsearch exception [type=cluster_block_exception, reason=index [xc_course] blocked by: [FORBIDDEN/12/index read-only / allow delete (api)];]，
该问题解决方法为：利用elasticsearch-head插件中的复合查询功能填写以下参数：http://47.111.242.221:9200/_all(所有索引，这里可指定单个索引名)   _settings    PUT    {"index.blocks.read_only_allow_delete":null}这四个参数后提交请求，若返回"acknowledged": true则ojbk


第三方的启动：
--1.elastic插件：在D:\elasticsearch-head目录下打开cmd窗口，执行npm run start
--2.logstash：在D:\elasticsearch\logstash-7.4.2\bin目录下打开cmd窗口，执行logstash.bat -f ../config/mysql.conf
logstash.bat -f ../config/mysql_course_media.conf



bug:
1.day12:xc-ui-pc-portal启动无法访问


p291 start



