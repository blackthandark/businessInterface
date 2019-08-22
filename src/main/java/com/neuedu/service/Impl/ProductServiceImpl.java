package com.neuedu.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.neuedu.common.ServerResponse;
import com.neuedu.consts.Const;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtils;
import com.neuedu.vo.ProductCategoryVO;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl implements IProductService{
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ICategoryService categoryService;
    @Value("${image.imageHost}")
    private String imageHost;
    @Override
    public ServerResponse list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //参数校验 keyword 和categoryId不能同时为空
        if(categoryId==null&&(keyword==null||"".equals(keyword))){
            return ServerResponse.createServerResponseByFail(1,"参数错误");
        }
        //根据categoryId查询
        Set<Integer> integerSet= Sets.newHashSet();
        if(categoryId!=null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && (keyword == null || "".equals(keyword))) {
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOList);
                return ServerResponse.createServerResponseBySuccess(pageInfo);
            }
            ServerResponse serverResponse = categoryService.get_deep_category(categoryId);

            if (serverResponse.isSuccess()) {
                integerSet = (Set<Integer>) serverResponse.getData();
            }
        }
        Page page=new Page();
        // 根据keyword查询
        if (keyword!=null&&!"".equals(keyword)){
            keyword="%"+keyword+"%";
        }
        if(orderBy.equals("")){
            page=PageHelper.startPage(pageNum,pageSize);
        }else{
            String[] orderByArr=orderBy.split("_");
            if(orderByArr.length>1){
                page=PageHelper.startPage(pageNum,pageSize,orderByArr[0]+" "+orderByArr[1]);
            }else{
                page=PageHelper.startPage(pageNum,pageSize);
            }
        }
        //List<Product>->List<ProductListVo>

        List<Product> productList=productMapper.searchProduct(integerSet,keyword);
        List<ProductListVO> productListVOList=Lists.newArrayList();
        if(productList!=null&&productList.size()>0){
            for(Product product:productList){
                if(product.getStatus()==Const.ProductStatus.PRODUCT_ONLINE.getCode()){
                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);}
            }
        }

        //分页
        PageInfo pageInfo=new PageInfo(page);
        //返回
        return ServerResponse.createServerResponseBySuccess(pageInfo);

    }

    @Override
    public ServerResponse detail_portal(Integer productId) {
        //参数校验
        if(productId==null){
            return ServerResponse.createServerResponseByFail(100,"参数不能为空");
        }
        //查询product
        Product product=productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createServerResponseByFail(2,"商品不存在");
        }
        //校验商品状态
        if(product.getStatus()!= Const.ProductStatus.PRODUCT_ONLINE.getCode()){
            return ServerResponse.createServerResponseByFail(4,"商品已下架或删除");
        }
        //获取productDetailVO
        ProductDetailVO productDetailVO=assembleProductDetailVO(product);
        //返回结果

        return ServerResponse.createServerResponseBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse topcategory(Integer sid) {
        //根据sid查询
        List<Category> categoryList=categoryMapper.selectChildCategory(sid);
        if(categoryList==null||categoryList.size()==0){
            return ServerResponse.createServerResponseBySuccess();
        }
        //转换为VO
        List<ProductCategoryVO> productCategoryVOList=Lists.newArrayList();
        for(Category category:categoryList){
            ProductCategoryVO productCategoryVO= assembleProductCategoryVO(category);
            productCategoryVOList.add(productCategoryVO);
        }
        //返回
        return ServerResponse.createServerResponseBySuccess(productCategoryVOList);
    }

    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }

    private ProductDetailVO assembleProductDetailVO(Product product) {
        ProductDetailVO productDetailVO=new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setId(product.getId());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setName(product.getName());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
        productDetailVO.setImageHost(imageHost);
        productDetailVO.setIs_banner(0);
        productDetailVO.setIs_hot(0);
        productDetailVO.setIs_new(0);
        return productDetailVO;
    }
    private ProductCategoryVO assembleProductCategoryVO(Category category){
        ProductCategoryVO productCategoryVO =new ProductCategoryVO();
        productCategoryVO.setId(category.getId());
        productCategoryVO.setName(category.getName());
        productCategoryVO.setParentId(category.getParentId());
        productCategoryVO.setSortOrder(category.getSortOrder());
        productCategoryVO.setStatus(category.getStatus());
        productCategoryVO.setCreateTime(DateUtils.dateToStr(category.getCreateTime()));
        productCategoryVO.setUpdateTime(DateUtils.dateToStr(category.getUpdateTime()));
        return productCategoryVO;
    }
}
