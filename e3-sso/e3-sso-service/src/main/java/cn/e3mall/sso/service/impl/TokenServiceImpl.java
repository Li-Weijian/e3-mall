package cn.e3mall.sso.service.impl;

import cn.e3.commom.jedis.JedisClient;
import cn.e3.commom.utils.E3Result;
import cn.e3.commom.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JedisClient jedisClient;

    /**
     * 根据token查询用户是否已经登录
     * */
    @Override
    public E3Result getUserByToken(String token) {

        //根据token去redis查询
        String user = jedisClient.get("SESSION:" + token);
        if ("".equals(user) || null == user){
            //查询不成功，返回用户登录已过期
            return E3Result.build(400,"用户登录已过期,请重新登录");
        }
        //如果查询成功，
            //重置token时间
        jedisClient.expire("SESSION:" + token,1800);
            //将json转换成User对象
        TbUser tbUser = JsonUtils.jsonToPojo(user, TbUser.class);
        return E3Result.ok(tbUser);
    }
}
