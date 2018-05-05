package cn.e3mall.content.redis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class TestJedis {

    //测试jedis连接单机版
    @Test
    public void testJedisCli(){
        Jedis jedis = new Jedis("192.168.25.131",6379);
        jedis.set("test","this is my first jedis");
        String test = jedis.get("test");
        System.out.println(test);
        jedis.close();
    }


    //测试jedis连接池连接
    @Test
    public void testJedisPool(){
        JedisPool pool = new JedisPool("192.168.25.131",6379);
        Jedis jedis = pool.getResource();
        String test = jedis.get("test");
        System.out.println(test);
        pool.close();
    }

    //测试jedis连接集群
    @Test
    public void testJedisCluster(){

        Set<HostAndPort> set = new HashSet<>();
        set.add(new HostAndPort("192.168.25.131",7001));
        set.add(new HostAndPort("192.168.25.131",7002));
        set.add(new HostAndPort("192.168.25.131",7003));
        set.add(new HostAndPort("192.168.25.131",7004));
        set.add(new HostAndPort("192.168.25.131",7005));
        set.add(new HostAndPort("192.168.25.131",7006));

        JedisCluster jedisCluster = new JedisCluster(set);
        jedisCluster.set("testCluster","hello jedisCluster");
        String cluster = jedisCluster.get("testCluster");
        System.out.println(cluster);

        jedisCluster.close();
    }

}
