package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.adapter.PackagingAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.exception.PackagingGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.PackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackagingServiceImpl implements PackagingService {
    private final PackagingAdapter packagingAdapter;

    // 모든 포장지 정보 가져오기
    @Override
    public List<PackagingGetResponse> getAllPackaging() {

        ResponseEntity<List<PackagingGetResponse>> response = packagingAdapter.getAllPackaging();
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new PackagingGetFailedException("포장지 정보를 가져올 수 없습니다.");
    }
}