package com.xuecheng.test.rabbitmq;

import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: 韩老魔
 * @Date: 2019/11/21 0021 17:14
 * 实现两个接口
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqTestApplication implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //AmqpTemplate好像不能ACK

    @Test
    public void testSendByTopics() {

        /*Mandatory为true时,消息通过交换器无法匹配到队列会返回给生产者，为false时,匹配不到会直接被丢弃。一般不需要设置*/
        this.rabbitTemplate.setMandatory(true);
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);
        this.rabbitTemplate.setConfirmCallback(this);

        //发送消息，参数：交换机、路由key、消息内容
        String message = "hello world";
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "inform.sms.email", message);
        System.out.println("Send Message is:'" + message + "'");
    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println("sender return success" + message.toString() + "\t" + i + "\t" + s1 + "\t" + s2);
    }

    /*确认后回调*/
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            System.out.println("消息发送失败" + cause + correlationData);
        } else {
            System.out.println("消息发送成功 ");
        }
    }
}
