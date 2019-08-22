package com.neuedu.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService{
    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        //参数非空校验
        if(shipping==null){
            return ServerResponse.createServerResponseByFail(100,"参数不能为空");
        }
        //添加
        shipping.setUserId(userId);
        shippingMapper.insert(shipping);

        //返回结果
        Map<String,Integer> map= Maps.newHashMap();
        map.put("shippingId",shipping.getId());
        return ServerResponse.createServerResponseBySuccess("新建地址成功",map);
    }

    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        //参数非空校验
        if(shippingId==null){
            return ServerResponse.createServerResponseByFail(100,"参数不能为空");
        }
        //删除
        int result=shippingMapper.deleteByUserIdAndShippingId(userId,shippingId);

        //返回
        if(result>0){
            return ServerResponse.createServerResponseBySuccess("删除地址成功");
        }else{
            return ServerResponse.createServerResponseByFail(1,"删除地址失败");
        }


    }

    @Override
    public ServerResponse update(Shipping shipping) {
        //参数非空判断
        if(shipping==null){
            return ServerResponse.createServerResponseByFail(100,"参数不能为空");
        }
        //更新
        int result=shippingMapper.updateBySelectiveKey(shipping);
        //返回
        if (result>0){
            return ServerResponse.createServerResponseBySuccess("更新地址成功");
        }else {
            return ServerResponse.createServerResponseByFail(1,"更新地址失败");
        }

    }

    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {
        //参数非空校验
        if(shippingId==null){
            return ServerResponse.createServerResponseByFail(100,"参数不能为空");
        }
        //查找
        Shipping shipping=shippingMapper.selectByUserIdAndShippingId(userId,shippingId);

        //返回
        if(shipping!=null){
            return ServerResponse.createServerResponseBySuccess(shipping);
        }else{
            return ServerResponse.createServerResponseByFail(1,"地址不存在");
        }
    }

    @Override
    public ServerResponse list(Integer userId, Integer pageNum, Integer pageSize) {
        Page page=PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList=shippingMapper.selectAllByUserId(userId);
        PageInfo pageInfo=new PageInfo(page);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }
}
