package com.nhnacademy.mini_dooray.ssacthree_front;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SsacthreeFrontApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        // 애플리케이션 컨텍스트가 정상적으로 로드되는지 확인
        assertThat(applicationContext).isNotNull();
    }
}
