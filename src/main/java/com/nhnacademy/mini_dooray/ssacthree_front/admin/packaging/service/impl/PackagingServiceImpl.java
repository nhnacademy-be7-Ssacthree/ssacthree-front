package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.adapter.PackagingAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.exception.PackagingCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.exception.PackagingGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.PackagingService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
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

    @Override
    public MessageResponse createPackaging(PackagingCreateRequest packagingCreateRequest) {
        ResponseEntity<MessageResponse> response = packagingAdapter.createPackaging(packagingCreateRequest);
        if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

        throw new PackagingCreateFailedException("포장지 추가에 실패했습니다.");
    }

    @Override
    public MessageResponse updatePackaging(Long packagingId, PackagingUpdateRequest packagingUpdateRequest) {
        ResponseEntity<MessageResponse> response = packagingAdapter.updatePackaging(packagingId, packagingUpdateRequest);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }

        throw new RuntimeException("포장지 수정에 실패했습니다.");
    }

    @Override
    public MessageResponse deletePackaging(Long packagingId) {
        // Adapter를 호출하여 delete 요청을 보냄
        ResponseEntity<MessageResponse> response = packagingAdapter.deletePackaging(packagingId);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }

        throw new RuntimeException("포장지 삭제에 실패했습니다.");
    }
}