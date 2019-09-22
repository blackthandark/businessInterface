package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    IProductService productService;
    @RequestMapping("list.do")
    public ServerResponse list(@RequestParam(name="categoryId",required = false) Integer categoryId,
                               @RequestParam(name="keyword",required = false)String keyword,
                               @RequestParam(name="pageNum",required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(name="pageSize",required = false,defaultValue = "10")Integer pageSize,
                               @RequestParam(name="orderBy",required = false,defaultValue = "")String orderBy){


        return productService.list(categoryId,keyword,pageNum,pageSize,orderBy);
    }
    @RequestMapping("detail.do")
    public ServerResponse detail(Integer productId){
        return productService.detail_portal(productId);
    }
    @RequestMapping("topcategory.do")
    public ServerResponse topcategory(@RequestParam(name="sid",required = false,defaultValue = "0")Integer sid){
        return productService.topcategory(sid);
    }
    @RequestMapping("gethot.do")
    public ServerResponse gethot(){

        return productService.gethot();
    }

}
