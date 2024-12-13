package com.nhnacademy.mini_dooray.ssacthree_front.elastic.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.SearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gateway-service", url = "http://localhost:8081/api/shop", contextId = "searchClient")  // 백엔드 API 호출
public interface SearchAdapter {

  @GetMapping("/search/books")  // 백엔드의 search/books API 호출
  SearchResponse searchBooks(@RequestParam String keyword,
                                 @RequestParam int page,
                                 @RequestParam(required = false) String sort,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestParam(required = false) String category,
                                 @RequestParam(required = false) String tag);

}
