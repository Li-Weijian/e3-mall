package cn.e3mall.content.redis;

import cn.e3.commom.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisClient {

    //测试Jedis整合spring
    @Test
    public void testJedisPool(){

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-redis.xml");
        JedisClient jedisClient = context.getBean(JedisClient.class);
        jedisClient.set("jedis-spring","hello");
        String s = jedisClient.get("jedis-spring");
        System.out.println(s);
    }
}
