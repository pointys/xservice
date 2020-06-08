package com.xuecheng.test.rabbitmq.send;

import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import com.xuecheng.test.rabbitmq.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Component
public class SendJob2 {
    @Value("${radom_value}")
    private String radom_value;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            if (ack) {
                System.out.println("消息发送成功" + "\tack:" + ack + "\tcause:" + cause + "\t" + correlationData);
            } else {
                System.out.println("消息发送失败" + "\tack:" + ack + "\tcause:" + cause + "\t" + correlationData);
            }
        }
    };
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.out.println("sender return message" + message + "\t" + replyCode + "\t" + replyText + "\t" + exchange + "\t" + routingKey);
        }
    };

    @Scheduled(fixedRate = 1000)
    public void publish() {
        Order order = new Order("1", "啊啊啊");

        //重写消息确认和返回
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        //发送消息，参数：交换机、路由key、消息内容、CorrelationData(全局唯一，根据该值找到消息)
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "inform.sms.email", order, new CorrelationData(radom_value));
        System.out.println("---Send Message is:'" + order + "'");
    }
}
