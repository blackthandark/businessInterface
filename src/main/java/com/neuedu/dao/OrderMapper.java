package com.neuedu.dao;

import com.neuedu.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface OrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbggenerated
     */
    int insert(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbggenerated
     */
    Order selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbggenerated
     */
    List<Order> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Order record);

    Order findOrderByUserIdAndOrderNo(@Param("userId")Integer userId,
                                      @Param("orderNo")Long orderNo);

    List<Order> findOrderByUserId(Integer userId);

    Order findOrderByOrderNo(Long orderNo);

    public List<Order> selectOrdersByCreateTime(@Param("time")String time);

    public Integer closeOrder(@Param("id")Integer id);

    List<Order> findOrderByUserIdAndStatus(@Param("userId")Integer userId,
                                           @Param("status")Integer status);
}