package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter.BookCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.request.BookLikeRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.response.BookLikeResponse;
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
    public Page<BookListResponse> getBooksByAuthorId(int page, int size, String[] sort, Long authorId) {
        try {
            ResponseEntity<Page<BookListResponse>> responseEntity = adapter.getBooksByAuthorId(page, size, sort, authorId);
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
    public Page<BookListResponse> getBooksByCategoryId(int page, int size, String[] sort, Long categoryId) {
        try {
            ResponseEntity<Page<BookListResponse>> responseEntity = adapter.getBooksByCategoryId(page, size, sort, categoryId);
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
    public Page<BookListResponse> getBooksByTagId(int page, int size, String[] sort, Long tagId) {
        try {
            ResponseEntity<Page<BookListResponse>> responseEntity = adapter.getBooksByTagId(page, size, sort, tagId);
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
        try {
            ResponseEntity<BookInfoResponse> responseEntity = adapter.getBookById(bookId);
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
    public List<CategoryNameResponse> getCategoriesByBookId(Long bookId) {
        try {
            ResponseEntity<List<CategoryNameResponse>> responseEntity = adapter.getCategoriesByBookId(bookId);
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
    public Page<BookListResponse> getAllAvailableBooks(int page, int size, String[] sort) {
        try {
            ResponseEntity<Page<BookListResponse>> responseEntity = adapter.getAllAvailableBooks(page, size, sort);
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
    public Page<BookListResponse> getBooksByMemberId(int page, int size, String[] sort) {
        try {
            ResponseEntity<Page<BookListResponse>> responseEntity = adapter.getBooksByMemberId(page, size, sort);
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
    public List<Long> getLikedBooksIdForCurrentUser() {
        try {
            ResponseEntity<List<Long>> responseEntity = adapter.getLikedBooksIdForCurrentUser();
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
    public BookLikeResponse createBookLikeByMemberId(BookLikeRequest bookLikeRequest) {
        try {
            ResponseEntity<BookLikeResponse> responseEntity = adapter.createBookLikeByMemberId(bookLikeRequest);
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
    public Boolean deleteBookLikeByMemberId(Long bookId) {
        try {
            ResponseEntity<Boolean> responseEntity = adapter.deleteBookLikeByMemberId(bookId);
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
}
