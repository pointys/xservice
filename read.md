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





