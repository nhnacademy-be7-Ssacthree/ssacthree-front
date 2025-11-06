package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.adapter.PackagingAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.adapter.PackagingCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.exception.PackagingCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.exception.PackagingDeleteFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.exception.PackagingGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.exception.PackagingUpdateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.PackagingService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.ResponseEntityHandler;
import com.nhnacademy.mini_dooray.ssacthree_front.image.adapter.ImageUploadAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackagingServiceImpl implements PackagingService {
    private final PackagingAdapter packagingAdapter;
    private final PackagingCustomerAdapter packagingCustomerAdapter;

    private  final ImageUploadAdapter imageUploadAdapter;

    // 모든 포장지 정보 가져오기
    @Override
    public List<PackagingGetResponse> getAllPackaging() {
        return ResponseEntityHandler.getResponseBody(
            packagingAdapter.getAllPackaging(),
            () -> new PackagingGetFailedException("포장지 정보를 가져올 수 없습니다.")
        );
    }

    @Override
    public List<PackagingGetResponse> getAllCustomerPackaging() {
        return ResponseEntityHandler.getResponseBody(
            packagingCustomerAdapter.getAllPackaging(),
            () -> new PackagingGetFailedException("포장지 정보를 가져올 수 없습니다.")
        );
    }

    @Override
    public MessageResponse createPackaging(PackagingCreateRequest packagingCreateRequest) {
        return ResponseEntityHandler.getResponseBody(
            packagingAdapter.createPackaging(packagingCreateRequest),
            () -> new PackagingCreateFailedException("포장지 추가에 실패했습니다.")
        );
    }

    @Override
    public MessageResponse updatePackaging(Long packagingId, PackagingUpdateRequest packagingUpdateRequest) {
        return ResponseEntityHandler.getResponseBody(
            packagingAdapter.updatePackaging(packagingId, packagingUpdateRequest),
            () -> new PackagingUpdateFailedException("포장지 수정에 실패했습니다.")
        );
    }

    @Override
    public String uploadImage(MultipartFile imageFile) {
        return imageUploadAdapter.uploadImage(imageFile);
    }

    @Override
    public MessageResponse deletePackaging(Long packagingId) {
        return ResponseEntityHandler.getResponseBody(
            packagingAdapter.deletePackaging(packagingId),
            () -> new PackagingDeleteFailedException("포장지 삭제에 실패했습니다.")
        );
    }
}