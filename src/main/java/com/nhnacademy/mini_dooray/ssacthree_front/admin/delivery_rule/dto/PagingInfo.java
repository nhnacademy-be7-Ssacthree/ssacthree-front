package com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingInfo {
  private int number;       // 현재 페이지
  private int size;         // 페이지 크기
  private int totalPages;   // 총 페이지 수
  private int totalElements; // 총 데이터 수
  private String sort;      // 정렬 옵션

}
