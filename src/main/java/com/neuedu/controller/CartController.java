package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.consts.Const;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
@CrossOrigin
@RestController
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    ICartService cartService;


    /*
    * 购物车中添加商品
    * */
    @RequestMapping("add.do")
    public ServerResponse add(Integer productId,
                              Integer count,
                              HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);


        return cartService.add(userInfo.getId(),productId,count);
    }


    @RequestMapping("list.do")
    public ServerResponse list(HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);


        return cartService.list(userInfo.getId());
    }

    @RequestMapping("update.do")
    public ServerResponse update(HttpSession session,
                                 Integer productId,
                                 Integer count){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);


        return cartService.update(userInfo.getId(),productId,count);
    }

    @RequestMapping("delete_product.do")
    public ServerResponse delete_product(HttpSession session,
                                         String productIds){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);


        return cartService.delete_product(userInfo.getId(),productIds);
    }

    @RequestMapping("select.do")
    public ServerResponse select(HttpSession session,
                                 Integer productId){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);


        return cartService.select(userInfo.getId(),productId,Const.CartCheckEnum.PRODUCT_CHECKED.getCode());
    }

    @RequestMapping("un_select.do")
    public ServerResponse un_select(HttpSession session,
                                 Integer productId){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);


        return cartService.select(userInfo.getId(),productId,Const.CartCheckEnum.PRODUCT_UNCHECKED.getCode());
    }

    @RequestMapping("select_all.do")
    public ServerResponse select_all(HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);


        return cartService.select(userInfo.getId(),null,Const.CartCheckEnum.PRODUCT_CHECKED.getCode());
    }

    @RequestMapping("un_select_all.do")
    public ServerResponse un_select_all(HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);


        return cartService.select(userInfo.getId(),null,Const.CartCheckEnum.PRODUCT_UNCHECKED.getCode());
    }

    @RequestMapping("get_cart_product_count.do")
    public ServerResponse get_cart_product_count(HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CURRENT_USER);


        return cartService.get_cart_product_count(userInfo.getId());
    }


}
