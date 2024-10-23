package com.nhnacademy.mini_dooray.ssacthree_front.controller.adapter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
@Component
public class Adapter {

    private final RestTemplate restTemplate;

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<List<T>> getList(String url, ParameterizedTypeReference<List<T>> responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, null, responseType);
    }

    public ResponseEntity<MessageDto> post(String url, Object request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.POST, entity, MessageDto.class);
    }

    public ResponseEntity<MessageDto> put(String url, Object request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.PUT, entity, MessageDto.class);
    }

    public ResponseEntity<MessageDto> delete(String url) {
        return restTemplate.exchange(url, HttpMethod.DELETE, null, MessageDto.class);
    }
}
