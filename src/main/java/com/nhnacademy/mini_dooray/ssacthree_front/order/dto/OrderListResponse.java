package com.nhnacademy.mini_dooray.ssacthree_front.order.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderListResponse {
  private Long orderId;
  private LocalDate orderDate;
  private int totalPrice;
  private String orderStatus;
}
