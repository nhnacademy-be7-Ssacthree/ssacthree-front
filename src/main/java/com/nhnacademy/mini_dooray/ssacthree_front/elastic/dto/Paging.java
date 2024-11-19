package com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Paging {
  private int number;       // 현재 페이지 번호 (0부터 시작)
  private int size;         // 페이지당 데이터 개수
  private int totalPages;   // 전체 페이지 수
  private String sort;      // 정렬 기준
}
