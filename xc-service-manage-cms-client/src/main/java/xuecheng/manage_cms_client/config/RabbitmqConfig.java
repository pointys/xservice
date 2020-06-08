package xuecheng.manage_cms_client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 韩浩辰
 * @date 2019/11/28 19:34
 */
@Configuration
public class RabbitmqConfig {

    //交换机bean的名称
    public static final String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";
    //队列bean的名称
    public static final String QUEUE_CMS_POSTPAGE = "queue_cms_postpage";

    //队列值
    @Value("${xuecheng.mq.queue}")
    public String queue_cms_postpage_name;
    //routingKey值 即站点Id
    @Value("${xuecheng.mq.routingKey}")
    public String routingKey;




    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EXCHANGE_TOPICS_INFORM() {
        return ExchangeBuilder.topicExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    //声明队列
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue QUEUE_CMS_POSTPAGE() {
        Queue queue = new Queue(queue_cms_postpage_name);
        return queue;
    }

    /**
     * 交换与队列通过路由key绑定
     * @param queue 队列bean的名称
     * @param exchange 交换机bean的名称
     * @return
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_CMS_POSTPAGE) Queue queue,
                                            @Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }


}
