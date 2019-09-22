package com.neuedu.service.Impl;

import com.google.common.collect.Sets;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService{
    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public ServerResponse get_category(Integer categoryId) {

        //参数非空校验
        if(categoryId==null){
            return ServerResponse.createServerResponseByFail(100,"参数不能为空");
        }
        //根据id查询类别
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category==null){
            return ServerResponse.createServerResponseByFail(1,"未找到该类别");
        }
        //查询子类别
        List<Category> categoryList=categoryMapper.selectChildCategory(categoryId);
        //返回结果
        return ServerResponse.createServerResponseBySuccess(categoryList);
    }

    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        //参数非空校验
        if(categoryId==null){
            return ServerResponse.createServerResponseByFail(100,"参数不能为空");
        }
        Set<Category> categorySet= Sets.newHashSet();
        categorySet=findAllChildCategory(categorySet,categoryId);
        Set<Integer> integerSet=Sets.newHashSet();
        Iterator<Category> categoryIterator=categorySet.iterator();
        while (categoryIterator.hasNext()){
            Category category=categoryIterator.next();
            integerSet.add(category.getId());
        }
        return ServerResponse.createServerResponseBySuccess(integerSet);
    }

    @Override
    public ServerResponse get_topcategory() {
        List<Category> categoryList=categoryMapper.selectTopCategory();
        if(categoryList==null||categoryList.size()<=0){
            return ServerResponse.createServerResponseByFail(100,"类别为空");
        }
        return ServerResponse.createServerResponseBySuccess(categoryList);
    }

    private Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        //查找categoryId下的子节点（平级）
        List<Category> categoryList=categoryMapper.selectChildCategory(categoryId);
        if(categoryList!=null&&categoryList.size()>0){
            for(Category category1:categoryList){
                findAllChildCategory(categorySet,category1.getId());
            }
        }
        return categorySet;
    }
}
