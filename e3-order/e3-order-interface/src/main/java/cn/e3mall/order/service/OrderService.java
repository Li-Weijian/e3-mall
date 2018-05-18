package cn.e3mall.order.service;

import cn.e3.commom.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;

public interface OrderService {

    public E3Result createOrder(OrderInfo orderInfo);

}
