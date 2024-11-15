package com.nhnacademy.mini_dooray.ssacthree_front.elastic.service;

import com.nhnacademy.mini_dooray.ssacthree_front.elastic.adapter.SearchAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.domain.BookDocument;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {

  private final SearchAdapter searchAdapter;


  public List<BookDocument> searchBooks(String keyword, int page, String sort, int pageSize, Map<String, String> filters) {
    // 전달 받은 값들을 사용해 쿼리 작성 및 API 호출

    // filters 맵에서 category와 tag 값을 가져와 개별 파라미터로 전달
    String category = filters.get("category");
    String tag = filters.get("tag");
    return searchAdapter.searchBooks(keyword, page, sort, pageSize, category, tag);
  }

}

