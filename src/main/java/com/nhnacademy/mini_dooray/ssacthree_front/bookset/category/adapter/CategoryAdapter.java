package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="gateway-service", url = "http://localhost:8081/api/shop", contextId = "categoryClient")
public interface CategoryAdapter {

    /**
     * 새로운 카테고리 생성 (관리자 전용)
     * @param request 카테고리 생성 요청 데이터
     * @return 생성된 카테고리 정보
     */
    @PostMapping("/admin/categories")
    ResponseEntity<CategoryInfoResponse> createCategory(@RequestBody CategorySaveRequest request);

    /**
     * 특정 카테고리 업데이트 (관리자 전용)
     * @param categoryId 업데이트할 카테고리 ID
     * @param request 업데이트 요청 데이터
     * @return 업데이트된 카테고리 정보
     */
    @PutMapping("/admin/categories/{categoryId}")
    ResponseEntity<CategoryInfoResponse> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryUpdateRequest request);

    /**
     * 특정 카테고리 소프트 삭제 (관리자 전용)
     * @param categoryId 소프트 삭제할 카테고리 ID
     * @return 삭제 상태 변경 결과
     */
    @DeleteMapping("/admin/categories/{categoryId}")
    ResponseEntity<Boolean> deleteCategory(@PathVariable Long categoryId);

    /**
     * 전체 카테고리 조회(관리자 전용)
     * @return 전체 카테고리 트리 정보
     * */
    @GetMapping("/admin/categories")
    ResponseEntity<List<CategoryInfoResponse>> getAllCategoriesForAdmin();

    /**
     * 전체 카테고리 트리 조회
     * @return 전체 카테고리 트리 정보
     */
    @GetMapping("/categories")
    ResponseEntity<List<CategoryInfoResponse>> getAllCategories();

    /**
     * 특정 ID를 가진 카테고리 조회
     * @param categoryId 카테고리 ID
     * @return 조회된 카테고리 정보
     */
    @GetMapping("/categories/{categoryId}")
    ResponseEntity<CategoryInfoResponse> getCategoryById(@PathVariable Long categoryId);

    /**
     * 주어진 부모 카테고리의 자식 카테고리 목록 조회
     * @param parentCategoryId 부모 카테고리 ID
     * @return 자식 카테고리 목록
     */
    @GetMapping("/categories/{parentCategoryId}/children")
    ResponseEntity<List<CategoryInfoResponse>> getChildCategories(@PathVariable Long parentCategoryId);

    /**
     * 최상위 카테고리 목록 조회
     * @return 최상위 카테고리 목록
     */
    @GetMapping("/categories/root")
    ResponseEntity<List<CategoryInfoResponse>> getRootCategories();

    /**
     * 카테고리 이름으로 검색
     * @param name 검색할 카테고리 이름
     * @return 검색된 카테고리 목록
     */
    @GetMapping("/categories/search")
    ResponseEntity<List<CategoryInfoResponse>> searchCategoriesByName(@RequestParam String name);

    /**
     * 특정 카테고리의 최상위 카테고리까지의 경로 조회
     * @param categoryId 카테고리 ID
     * @return 최상위 카테고리까지의 경로
     */
    @GetMapping("/categories/{categoryId}/path")
    ResponseEntity<List<CategoryInfoResponse>> getCategoryPath(@PathVariable Long categoryId);

    /**
     * 특정 카테고리의 지정 깊이의 하위 카테고리 조회
     * @param categoryId 조회할 카테고리 ID
     * @param depth 조회 깊이
     * @return 지정 깊이의 하위 카테고리 목록
     */
    @GetMapping("/categories/{categoryId}/children/depth/{depth}")
    ResponseEntity<List<CategoryInfoResponse>> getCategoryWithChildren(@PathVariable Long categoryId, @PathVariable int depth);

    /**
     * 특정 카테고리의 모든 하위 카테고리 조회
     * @param categoryId 조회할 카테고리 ID
     * @return 모든 하위 카테고리 목록
     */
    @GetMapping("/categories/{categoryId}/descendants")
    ResponseEntity<List<CategoryInfoResponse>> getAllDescendants(@PathVariable Long categoryId);
}
