package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressResponse {

    private long addressId;
    private String addressAlias;
    private String addressRoadname;
    private String addressDetail;
    private String addressPostalNumber;
}
