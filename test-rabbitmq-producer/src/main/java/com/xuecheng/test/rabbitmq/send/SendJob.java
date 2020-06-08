package com.xuecheng.test.rabbitmq.send;

import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import com.xuecheng.test.rabbitmq.model.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.SimpleFormatter;

@Component
public class SendJob implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    @Value("${radom_value}")
    private String radom_value;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedRate = 1000)
    public void publish() {
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);

        //发送消息，参数：交换机、路由key、消息内容、CorrelationData(全局唯一，根据该值找到消息)
        Order order = new Order("贺友义", "0.1厘米");
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "inform.sms.email", order,new CorrelationData(radom_value));
        System.out.println("---Send Message is:'" + order + "'");
    }

    /*确认后回调*/
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            System.out.println("消息发送失败" + "\tack:" + ack + "\tcause:"  + cause + "\t" + correlationData);
        } else {
            System.out.println("消息发送成功 " + "\tack:" + ack + "\tcause:" + cause + "\t" + correlationData);
        }
    }

    /*发送失败会调用该方法，如交换机绑定路由失败*/
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.err.println("sender return seem little problem：" + message + "\t" + replyCode + "\t" + replyText + "\t" + exchange + "\t" + routingKey);
    }

}
