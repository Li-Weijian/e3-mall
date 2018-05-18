package cn.e3mall.order.service.impl;

import cn.e3.commom.jedis.JedisClient;
import cn.e3.commom.utils.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;

    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;
    @Value("${ORDER_START_VALUE}")
    private String ORDER_START_VALUE;
    @Value("${ORDER_ITEM_ID}")
    private String ORDER_ITEM_ID;

    /**
     * 创建新订单
     * */
    @Override
    public E3Result createOrder(OrderInfo orderInfo) {

        //创建OrderId，使用redis自增的方式
        if (!jedisClient.exists(ORDER_GEN_KEY)){
            //不存在，设置起始值
            jedisClient.set(ORDER_GEN_KEY,ORDER_START_VALUE);
        }
        String orderId = jedisClient.incr(ORDER_GEN_KEY).toString();

        //完善Order的字段
        orderInfo.setOrderId(orderId);
        // status  1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        //写入Order表
        orderMapper.insert(orderInfo);

        // OrderItem -- 订单明细表
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            // id 明细表的id
            String orderItemId = jedisClient.incr(ORDER_ITEM_ID).toString();
            orderItem.setId(orderItemId);
            // orderId
            orderItem.setOrderId(orderId);
            //写入订单明细表
            orderItemMapper.insert(orderItem);
        }

        //OrderShipping表
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insert(orderShipping);

        return E3Result.ok(orderId);
    }
}
