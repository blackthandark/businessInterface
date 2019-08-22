package com.neuedu.service.Impl;

import com.google.common.collect.Lists;
import com.neuedu.common.ServerResponse;
import com.neuedu.consts.Const;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.service.ICategoryService;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.vo.CartProductVO;
import com.neuedu.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService{
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    @Override
    public ServerResponse add(Integer userId,Integer productId, Integer count) {
        //参数非空校验
        if(productId==null||count==null){
            return ServerResponse.createServerResponseByFail(9,"参数不能为空");
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createServerResponseByFail(2,"商品不存在");
        }

        //根据productId和userId查询购物信息
        Cart cart=cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if(cart==null){
            //添加
            Cart cart1=new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckEnum.PRODUCT_CHECKED.getCode());
            cartMapper.insert(cart1);
        }else {
            //更新
            Cart cart1=new Cart();
            cart1.setId(cart.getId());
            cart1.setProductId(productId);
            cart1.setUserId(userId);
            cart1.setQuantity(count);
            cart1.setChecked(cart.getChecked());
            cartMapper.updateByPrimaryKey(cart1);
        }

        CartVO cartVO=getCartVOLimit(userId);
        //
        return ServerResponse.createServerResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse list(Integer userId) {
        CartVO cartVO=getCartVOLimit(userId);
        if(cartVO==null){
            return ServerResponse.createServerResponseByFail(1,"还没选中任何商品");
        }
        return ServerResponse.createServerResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {
        //参数非空判断
        if(productId==null||count==null){
            return ServerResponse.createServerResponseByFail(9,"参数不能为空");
        }
        //查询购物车中商品
        Cart cart=cartMapper.selectCartByUserIdAndProductId(userId,productId);
        if(cart!=null){
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
        //更新数量
        //返回cartVO
        return ServerResponse.createServerResponseBySuccess(getCartVOLimit(userId));
    }

    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
        //参数非空校验
        if(productIds==null||"".equals(productIds)){
            return ServerResponse.createServerResponseByFail(9,"参数不能为空");
        }
        //productIds->List<Interger>
        List<Integer> productIdList=Lists.newArrayList();
        String[] productIdArr=productIds.split(",");
        if(productIdArr!=null&&productIdArr.length>0){
            for(String productIdstr:productIdArr){
                Integer productId=Integer.parseInt(productIdstr);
                productIdList.add(productId);
            }
        }
        //调用dao
        cartMapper.deleteByUserIdAndProductIds(userId,productIdList);



        //返回结果
        return ServerResponse.createServerResponseBySuccess(getCartVOLimit(userId));
    }

    @Override
    public ServerResponse select(Integer userId, Integer productId,Integer check) {

        //调用dao
        cartMapper.selectOrUnselectProduct(userId,productId,check);

        //返回结果
        return ServerResponse.createServerResponseBySuccess(getCartVOLimit(userId));
    }

    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
        int quantity=cartMapper.get_cart_product_count(userId);
        return ServerResponse.createServerResponseBySuccess(quantity);
    }

    private CartVO getCartVOLimit(Integer userId) {
        CartVO cartVO = new CartVO();
        //根据userId查询购物信息
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        //List<Cart>->List<CartProductVO>
        List<CartProductVO> cartProductVOList = Lists.newArrayList();

        BigDecimal carttotalprice = new BigDecimal("0");

        if (cartList != null && cartList.size() > 0) {
            for (Cart cart : cartList) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cart.getId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setUserId(userId);
                cartProductVO.setProductChecked(cart.getChecked());
                //查询商品
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product != null) {
                    cartProductVO.setProductId(cart.getProductId());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductStock(product.getStock());
                    cartProductVO.setProductSubtitle(product.getSubtitle());

                    int stock = product.getStock();
                    int limitProductCount = 0;
                    if (stock >= cart.getQuantity()) {
                        limitProductCount = cart.getQuantity();
                        cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");

                    } else {
                        limitProductCount = stock;
                        //更新购物车中的商品数量
                        Cart cart1 = new Cart();
                        cart1.setId(cart.getId());
                        cart1.setQuantity(stock);
                        cart1.setProductId(cart.getProductId());
                        cart1.setChecked(cart.getChecked());
                        cart1.setUserId(userId);
                        cartMapper.updateByPrimaryKey(cart1);
                        cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
                    }

                    cartProductVO.setQuantity(limitProductCount);
                    cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(), Double.valueOf(cartProductVO.getQuantity())));


                }
                if(cartProductVO.getProductChecked()==Const.CartCheckEnum.PRODUCT_CHECKED.getCode()){
                    carttotalprice = BigDecimalUtils.add(carttotalprice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
                }

                cartProductVOList.add(cartProductVO);

            }
        }


            cartVO.setCartProductVOList(cartProductVOList);
            //计算总价格
            cartVO.setCarttotalprice(carttotalprice);
            //判断购物车是否全选
            int count=cartMapper.isCheckedAll(userId);
            if(count>0){
                cartVO.setIsallchecked(false);
            }else{
                cartVO.setIsallchecked(true);
            }
            //返回结果

            return cartVO;
        }

}
