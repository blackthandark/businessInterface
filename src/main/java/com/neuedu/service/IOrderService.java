package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Order;

import java.util.List;
import java.util.Map;

public interface IOrderService {
    public ServerResponse create(Integer userId,Integer shippingId);

    public ServerResponse cancel(Integer userId,Long orderNo);

    public ServerResponse get_order_cart_product(Integer userId);

    public ServerResponse list(Integer userId,Integer pageNum,Integer pageSize,Integer status);

    public ServerResponse detail(Long orderNo);

    public ServerResponse pay(Integer userId,Long orderNo);

    /*
    * 支付宝回调
    * */
    public ServerResponse alipay_callback(Map<String,String> map);

    public ServerResponse query_order_pay_status(Integer userId,Long orderNo);

    public List<Order> closeOrder(String closeOrderDate);

    ServerResponse listall(Integer userId, Integer status);
}
