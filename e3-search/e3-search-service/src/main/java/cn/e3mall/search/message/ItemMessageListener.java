package cn.e3mall.search.message;

import cn.e3mall.search.service.SearchItemService;
import cn.e3mall.search.service.impl.SearchItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 商品消息监听器
 * */
public class ItemMessageListener implements MessageListener {
    @Autowired
    private SearchItemServiceImpl searchItemService;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage){
            TextMessage textMessage = (TextMessage) message;
            try {
                String itemId = textMessage.getText();
                Thread.sleep(1000);
                searchItemService.addDocumentById(Long.parseLong(itemId));
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
