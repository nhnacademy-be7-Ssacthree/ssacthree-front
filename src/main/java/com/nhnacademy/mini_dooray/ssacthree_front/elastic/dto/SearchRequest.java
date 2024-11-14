package com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class SearchRequest {
  private String keyword;    // 검색 키워드
  private int page;          // 페이지 번호
  private String sort;       // 정렬 기준
  private Map<String, String> filters; // 필터 (카테고리, 태그 등)
}
