package com.nhnacademy.mini_dooray.ssacthree_front.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponseDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        // CategoryInfoResponse에 역직렬화를 적용하기 위해서.
        module.addDeserializer(CategoryInfoResponse.class, new CategoryInfoResponseDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}

