package cn.e3mall.sso.service;

import cn.e3.commom.utils.E3Result;

public interface TokenService {

    public E3Result getUserByToken(String token);

}
