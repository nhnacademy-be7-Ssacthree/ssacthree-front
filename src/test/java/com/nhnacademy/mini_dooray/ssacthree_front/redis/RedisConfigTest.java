//package com.nhnacademy.mini_dooray.ssacthree_front.redis;
//
//import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
//import com.nhnacademy.mini_dooray.ssacthree_front.config.RedisConfig;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.context.ContextConfiguration;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@ContextConfiguration(classes = {RedisConfigTest.RedisTestConfig.class, RedisConfig.class})
//class RedisConfigTest {
//
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Autowired
//    private RedisConnectionFactory redisConnectionFactory;
//
//    @Configuration
//    static class RedisTestConfig {
//        @Bean
//        public RedisProperties redisProperties() {
//            RedisProperties redisProperties = new RedisProperties();
//            redisProperties.setHost("localhost");
//            redisProperties.setPort(6379);
//            return redisProperties;
//        }
//    }
//
//    @Test
//    void testRedisTemplate() {
//        assertThat(redisConnectionFactory).isNotNull();
//        assertThat(redisTemplate).isNotNull();
//
//        CartItem cartItem = new CartItem(1L, "책 제목", 2, 30000, null);
//        redisTemplate.opsForValue().set("test:cartItem", cartItem);
//
//        CartItem retrievedCartItem = (CartItem) redisTemplate.opsForValue().get("test:cartItem");
//        assertThat(retrievedCartItem).isNotNull();
//        assertThat(retrievedCartItem.getId()).isEqualTo(cartItem.getId());
//        assertThat(retrievedCartItem.getTitle()).isEqualTo(cartItem.getTitle());
//        assertThat(retrievedCartItem.getQuantity()).isEqualTo(cartItem.getQuantity());
//        assertThat(retrievedCartItem.getPrice()).isEqualTo(cartItem.getPrice());
//    }
//}
