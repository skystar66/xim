package com.xuliang.cim.server.token;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TokenHelper {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public String getToken(Long userId) {
        return stringRedisTemplate.opsForValue().get(userId.toString());
    }

    public void setToken(Long userId, String token) {
        stringRedisTemplate.opsForValue().set(userId.toString(), token);
    }

    public boolean checkToken(Long userId, String token) {
        return stringRedisTemplate.opsForValue().get(userId.toString()).equals(token);
    }


    public boolean clearToken(Long userId) {
        return stringRedisTemplate.delete(userId.toString());
    }

}
