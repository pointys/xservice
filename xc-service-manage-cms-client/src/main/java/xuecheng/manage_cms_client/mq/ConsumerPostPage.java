package xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xuecheng.manage_cms_client.service.PageService;

import java.io.IOException;
import java.util.Map;

/**
 * @author 韩浩辰
 * 用来通知发布页面的中间消息件
 * @date 2020/4/15 16:53
 */
@Component
@Slf4j
public class ConsumerPostPage {
    @Autowired
    private PageService pageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg, Channel channel, Message message) {
        //解析消息中的pageId
        Map map = JSON.parseObject(msg, Map.class);
        String pageId = (String) map.get("pageId");
        log.info("消费方接收消息："+pageId);
        //校验页面是否存在
        CmsPage cmsPage = pageService.findCmsPageByPageId(pageId);
        if(cmsPage==null){
            log.error("pageId为"+pageId+"的页面不存在");
            return;
        }
        //将html文件模板从GridFs中下载到服务器
        pageService.savePageToServerPath(pageId);

        //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 重启消费端服务后续还会接收
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("receiver success,接收到的消息为：" + msg);
        } catch (Exception e) {
            //丢弃这条消息，避免堵塞
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
