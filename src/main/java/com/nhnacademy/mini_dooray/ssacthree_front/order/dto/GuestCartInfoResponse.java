package com.nhnacademy.mini_dooray.ssacthree_front.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 일단 안쓰고 병현님꺼 이용. 이걸로 하면 안되나? 그리고 book정보는 음
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GuestCartInfoResponse {
    //상품 정보만..상품 아이디랑 수량
    private long bookId;
    private int bookQuentity;

}
