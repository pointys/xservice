package com.xuecheng.test.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import com.xuecheng.test.rabbitmq.model.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReceiveHandler {

    /**
     * 消费方
     *
     * @param message
     * @param channel：手动ACK导包，rabbitmq.client.Channel
     */
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL,RabbitmqConfig.QUEUE_INFORM_SMS})
    @RabbitHandler
    public void reveive(Message message, Channel channel, Order order) throws IOException {
        try {
            //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 重启消费端服务后续还会接收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            System.out.println("receiver success,接收到的消息为：" + order.toString());
        } catch (Exception e) {
            e.printStackTrace();
            //丢弃这条消息，避免堵塞
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            System.out.println("receiver fail:" + e);
        }
    }
}
