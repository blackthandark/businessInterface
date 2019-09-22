package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.consts.Const;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
@CrossOrigin
@RestController
@RequestMapping("/shipping/")
public class AddressController {
    @Autowired
    IAddressService addressService;

    /*
    * 添加地址
    * */
    @RequestMapping("add.do")
    public ServerResponse add(HttpSession session,
                              Shipping shipping){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(1,"用户未登录，请登录");
        }
        return addressService.add(userInfo.getId(),shipping);
    }
    /*
    * 删除地址
    * */
    @RequestMapping("del.do")
    public ServerResponse del(HttpSession session,
                              Integer shippingId){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(1,"用户未登录，请登录");
        }
        return addressService.del(userInfo.getId(),shippingId);
    }

    /*
    * 更新地址
    * */
    @RequestMapping("update.do")
    public ServerResponse update(HttpSession session,
                              Shipping shipping){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(1,"用户未登录，请登录");
        }
        shipping.setUserId(userInfo.getId());
        return addressService.update(shipping);
    }

    /*
    * 查看具体的地址
    * */
    @RequestMapping("select.do")
    public ServerResponse select(HttpSession session,
                              Integer shippingId){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(1,"用户未登录，请登录");
        }
        return addressService.select(userInfo.getId(),shippingId);
    }
    /*
    * 地址列表
    * */
    @RequestMapping("list.do")
    public ServerResponse list(HttpSession session,
                                 @RequestParam(name = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                 @RequestParam(name="pageSize",required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(1,"用户未登录，请登录");
        }
        return addressService.list(userInfo.getId(),pageNum,pageSize);
    }

}
