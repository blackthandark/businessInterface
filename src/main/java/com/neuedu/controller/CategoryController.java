package com.neuedu.controller;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/category/")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;
    @RequestMapping("get_category.do")
    public ServerResponse get_category(Integer categoryId){
        ServerResponse serverResponse=categoryService.get_category(categoryId);
        return serverResponse;
    }

    @RequestMapping("get_deep_category.do")
    public ServerResponse get_deep_category(Integer categoryId){
        ServerResponse serverResponse=categoryService.get_deep_category(categoryId);
        return serverResponse;
    }
    @RequestMapping("get_topcategory.do")
    public ServerResponse get_topcategory(){
        ServerResponse serverResponse=categoryService.get_topcategory();
        return serverResponse;
    }


}
