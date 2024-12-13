package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PointHistoryGetResponse;
import org.springframework.data.domain.Page;

public interface PointHistoryService {

    Page<PointHistoryGetResponse> getPointHistory(Integer page,
        Integer size, String sort, String direction);
}
