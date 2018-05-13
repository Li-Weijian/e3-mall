package cn.e3mall.sso.service;

import cn.e3.commom.utils.E3Result;
import cn.e3mall.pojo.TbUser;

public interface RegisterService {

    public E3Result checkData(String data, Integer type);

    public E3Result register(TbUser user);

}
