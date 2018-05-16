package cn.e3mall.cart.service;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestItem {

    @Test
    public void test(){

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classath:spring/springmvc.xml");
        CartService service = applicationContext.getBean(CartService.class);
        service.addCart((long) 536563,1,2);

    }


}
