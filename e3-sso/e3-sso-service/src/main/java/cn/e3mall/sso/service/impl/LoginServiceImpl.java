package cn.e3mall.sso.service.impl;

import cn.e3.commom.jedis.JedisClient;
import cn.e3.commom.utils.E3Result;
import cn.e3.commom.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;

    /**
     * 登录
     * */
    @Override
    public E3Result login(String username, String password) {

        TbUserExample userExample = new TbUserExample();
        TbUserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> userList = userMapper.selectByExample(userExample);
//      1、判断用户名密码是否正确。
        if (userList == null || userList.size() == 0) {
            return E3Result.build(400, "用户名或密码错误");
        }
        TbUser user = userList.get(0);
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            return E3Result.build(400,"用户名或密码错误");
        }

//      2、登录成功后生成token。Token相当于原来的jsessionid，字符串，可以使用uuid。
        String token = UUID.randomUUID().toString();

//      3、把用户信息保存到redis。Key就是token，value就是TbUser对象转换成json。
//      4、使用String类型保存Session信息。可以使用“前缀:token”为key
        user.setPassword(null);  //保护用户安全，不需要存储密码
        jedisClient.set("SESSION:"+token,JsonUtils.objectToJson(user));
//      5、设置key的过期时间。模拟Session的过期时间。一般半个小时。
        jedisClient.expire("SESSION:"+token,1800);
//      6、返回e3Result包装token。
        return E3Result.ok(token);
    }
}
