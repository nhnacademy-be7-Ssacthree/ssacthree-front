package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter.BookCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCommonServiceImpl implements BookCommonService {

    private final BookCustomerAdapter adapter;

    @Override
    public Page<BookInfoResponse> getBooksByAuthorId(int page, int size, String[] sort, Long authorId) {
        try {
            ResponseEntity<Page<BookInfoResponse>> responseEntity = adapter.getBooksByAuthorId(page, size, sort, authorId);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                // 필요한 에러 처리 로직 추가
                throw new RuntimeException("API 호출 실패: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            // 예외 로깅 및 처리
            throw new RuntimeException("API 호출 중 예외 발생", e);
        }
    }

    @Override
    public BookInfoResponse getBookById(Long bookId) {
        return adapter.getBookById(bookId).getBody();
    }

    @Override
    public List<CategoryNameResponse> getCategoriesByBookId(Long bookId) {
        return adapter.getCategoriesByBookId(bookId).getBody();
    }

    @Override
    public Page<BookInfoResponse> getAllAvailableBooks(int page, int size, String[] sort) {
        return adapter.getRecentBooks(page, size, sort).getBody();
    }
}
