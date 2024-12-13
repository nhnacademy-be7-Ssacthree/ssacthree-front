package com.nhnacademy.mini_dooray.ssacthree_front.elastic.service;

import com.nhnacademy.mini_dooray.ssacthree_front.elastic.adapter.SearchAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.SearchResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchService {

  private final SearchAdapter searchAdapter;


  public SearchResponse searchBooks(String keyword, int page, String sort, int pageSize, Map<String, String> filters) {
    // 전달 받은 값들을 사용해 쿼리 작성 및 API 호출

    return searchAdapter.searchBooks(keyword, page, sort, pageSize,
        filters.get("category"), // 필터에서 category 사용
        filters.get("tag")       // 필터에서 tag 사용
    );
  }

}

