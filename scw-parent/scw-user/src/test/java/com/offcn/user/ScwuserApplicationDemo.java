package com.offcn.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserApp.class})
public class ScwuserApplicationDemo {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void redisDemo(){
        redisTemplate.opsForValue().set("msg","通过StringRedis储存的内容");
    }
}
