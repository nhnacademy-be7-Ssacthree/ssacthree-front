package com.nhnacademy.mini_dooray.ssacthree_front.order.exception;

public class FailedCreateOrder extends RuntimeException {
  public FailedCreateOrder(String message) {
    super(message);
  }
}
