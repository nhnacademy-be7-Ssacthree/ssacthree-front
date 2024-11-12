package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import java.util.List;

public interface PackagingService {

    List<PackagingGetResponse> getAllPackaging();

    MessageResponse createPackaging(PackagingCreateRequest packagingCreateRequest);

    MessageResponse deletePackaging(Long packagingId);

    MessageResponse updatePackaging(Long packagingId, PackagingUpdateRequest packagingUpdateRequest);
}
