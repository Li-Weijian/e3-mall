package cn.e3mall.sso.service.impl;

import cn.e3.commom.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper userMapper;



    /**
     * 校验注册数据
     * */
    @Override
    public E3Result checkData(String data, Integer type) {

        TbUserExample userExample = new TbUserExample();
        TbUserExample.Criteria criteria = userExample.createCriteria();

        if (type == 1){
            //验证用户名
            criteria.andUsernameEqualTo(data);
        }else if (type == 2){
            //验证手机号
            criteria.andPhoneEqualTo(data);
        }/*else if (type == 3){
            //验证邮箱
            criteria.andEmailEqualTo(data);
        }*/

        List<TbUser> userList = userMapper.selectByExample(userExample);
        if (userList.size() > 0 || userList == null){
            return E3Result.ok(false);
        }
        return E3Result.ok(true);
    }

    /**
     * 用户注册
     * */
    @Override
    public E3Result register(TbUser user) {

        //校验用户
        if (user != null){
            if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone())){
                return E3Result.build(400,"用户数据不完整");
            }
            //校验用户名、手机号码、邮箱是否有误
            E3Result result = checkData(user.getUsername(), 1);
            if ((boolean)result.getData() != true) {
                return E3Result.build(400, "用户名或密码错误");
            }
            result = checkData(user.getPhone(),2);
            if ((boolean)result.getData() != true){
                return E3Result.build(400,"手机号已被使用");
            }
          /*  result = checkData(user.getEmail(),3);
            if ((boolean)result.getData() != true){
                return E3Result.build(400,"邮箱已被使用");
            }*/
            //补全用户字段
            user.setCreated(new Date());
            user.setUpdated(new Date());
//            user.setEmail("123@qq.com");
            //使用md5进行密码加密
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            //插入数据库
            userMapper.insertSelective(user);
            return E3Result.ok();
        }
        return null;
    }
}
