package com.neuedu.controller;

import com.neuedu.redis.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Controller
@RequestMapping("/test/")
public class TestController {
    @Autowired
    RedisProperties redisProperties;
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("getredis")
    public void test(){
        System.out.println(redisProperties.getRedisip());
    }
    @RequestMapping("redis")
    public String getJedis(){
        Jedis jedis=jedisPool.getResource();
        String value=jedis.set("root","root1");
        jedisPool.returnResource(jedis);
        return value;
    }







}
