package com.nhnacademy.mini_dooray.ssacthree_front.commons.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
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

    public ResponseEntity<MessageResponse> post(String url, Object request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.POST, entity, MessageResponse.class);
    }

    public ResponseEntity<MessageResponse> put(String url, Object request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(request, httpHeaders);

        return restTemplate.exchange(url, HttpMethod.PUT, entity, MessageResponse.class);
    }

    public ResponseEntity<MessageResponse> delete(String url) {
        return restTemplate.exchange(url, HttpMethod.DELETE, null, MessageResponse.class);
    }
}
