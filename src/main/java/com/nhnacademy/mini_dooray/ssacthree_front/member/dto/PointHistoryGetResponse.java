package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PointHistoryGetResponse {

    private Integer pointAmount;
    private LocalDateTime pointChangeDate;
    private String pointChangeReason;
}
