package com.xuecheng.test.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import com.xuecheng.test.rabbitmq.model.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

//@Component
public class ReceiveHandler2 {

    /**
     * 消费方
     *
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL, RabbitmqConfig.QUEUE_INFORM_SMS})
    @RabbitHandler
    public void reveive(Channel channel, Order order, Message message) throws IOException {

        //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉,否则消息仍存在于队列当中，重新启动该程序队列中的消息会再次被监听
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        System.out.println("receiver success,接收到的消息为：" + message);
    }
}
