package com.nhnacademy.mini_dooray.ssacthree_front.elastic.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.elastic.domain.BookDocument;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gateway-service", url = "http://localhost:8081/api/shop", contextId = "searchClient")  // 백엔드 API 호출
public interface SearchAdapter {

//  @GetMapping("/search/books")  // 백엔드의 search/books API 호출
//  List<BookDocument> searchBooks(@RequestBody SearchRequest searchRequest);  // 검색 요청을 전달

  @GetMapping("/search/books")  // 백엔드의 search/books API 호출
  List<BookDocument> searchBooks(@RequestParam String keyword,
                                 @RequestParam int page,
                                 @RequestParam(required = false) String sort,
                                 @RequestParam(required = false) int pageSize,
                                 @RequestParam(required = false) String category,
                                 @RequestParam(required = false) String tag);

}
