package cn.e3mall.sso.service;

import cn.e3.commom.utils.E3Result;

public interface LoginService {

    public E3Result login(String username, String password);

}
