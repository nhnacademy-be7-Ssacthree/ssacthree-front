package com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto;

import com.nhnacademy.mini_dooray.ssacthree_front.elastic.domain.BookDocument;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
* 페이징 처리를 위한 totalHist 를 담은 검색 응답 DTO 입니다
*
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
  private Integer totalHits;             // 전체 데이터 개수
  private List<BookDocument> books;   // 검색된 책 리스트
}