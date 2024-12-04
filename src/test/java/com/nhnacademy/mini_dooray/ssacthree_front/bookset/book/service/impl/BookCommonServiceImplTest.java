package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter.BookCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.exception.BookFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.request.BookLikeRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.response.BookLikeResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class BookCommonServiceImplTest {

    @Mock
    private BookCustomerAdapter adapter;

    @InjectMocks
    private BookCommonServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 리플렉션을 사용하여 private 필드 설정
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 샘플 BookListResponse 객체 생성 (리플렉션 사용)
    private BookListResponse createSampleBookListResponse() {
        BookListResponse response = new BookListResponse();
        setField(response, "bookId", 1L);
        setField(response, "bookName", "Sample Book");
        setField(response, "publicationDate", LocalDateTime.now());
        setField(response, "regularPrice", 20000);
        setField(response, "salePrice", 15000);
        setField(response, "bookThumbnailImageUrl", "http://example.com/image.jpg");
        setField(response, "bookViewCount", 100);
        setField(response, "bookDiscount", 25);
        setField(response, "bookStatus", "AVAILABLE");
        setField(response, "likeCount", 10L);
        setField(response, "reviewCount", 5L);
        setField(response, "reviewRateAverage", 4.5);
        return response;
    }

    // 샘플 BookInfoResponse 객체 생성 (Builder 사용)
    private BookInfoResponse createSampleBookInfoResponse() {
        return BookInfoResponse.builder()
            .bookId(1L)
            .bookName("Sample Book")
            .bookIndex("Sample Index")
            .bookInfo("Sample Info")
            .bookIsbn("123-4567890123")
            .publicationDate(LocalDateTime.now())
            .regularPrice(20000)
            .salePrice(15000)
            .isPacked(true)
            .stock(50)
            .bookThumbnailImageUrl("http://example.com/image.jpg")
            .bookViewCount(100)
            .bookDiscount(25)
            .bookStatus("AVAILABLE")
            .build();
    }

    @Nested
    @DisplayName("getBooksByAuthorId 테스트")
    class GetBooksByAuthorIdTests {

        @Test
        @DisplayName("API 호출이 성공하면 책 목록 페이지를 반환해야 한다")
        void testGetBooksByAuthorIdSuccess() {
            // Arrange
            int page = 0;
            int size = 10;
            String[] sort = {"name,asc"};
            Long authorId = 1L;

            BookListResponse sampleBook = createSampleBookListResponse();
            List<BookListResponse> books = Arrays.asList(sampleBook);
            Page<BookListResponse> bookPage = new PageImpl<>(books);

            when(adapter.getBooksByAuthorId(page, size, sort, authorId))
                .thenReturn(new ResponseEntity<>(bookPage, HttpStatus.OK));

            // Act
            Page<BookListResponse> result = service.getBooksByAuthorId(page, size, sort, authorId);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(adapter, times(1)).getBooksByAuthorId(page, size, sort, authorId);
        }


        @Test
        @DisplayName("Adapter가 예외를 던지면 RuntimeException을 던져야 한다")
        void testGetBooksByAuthorIdAdapterException() {
            // Arrange
            int page = 0;
            int size = 10;
            String[] sort = {"name,asc"};
            Long authorId = 1L;

            when(adapter.getBooksByAuthorId(page, size, sort, authorId))
                .thenThrow(new BookFailedException("Adapter error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.getBooksByAuthorId(page, size, sort, authorId));

            assertTrue(exception.getMessage().contains("저자로 책들을 조회하는데 실패하였습니다."));
            verify(adapter, times(1)).getBooksByAuthorId(page, size, sort, authorId);
        }
    }

    @Nested
    @DisplayName("getBooksByCategoryId 테스트")
    class GetBooksByCategoryIdTests {

        @Test
        @DisplayName("API 호출이 성공하면 책 목록 페이지를 반환해야 한다")
        void testGetBooksByCategoryIdSuccess() {
            // Arrange
            int page = 0;
            int size = 10;
            String[] sort = {"name,asc"};
            Long categoryId = 1L;

            BookListResponse sampleBook = createSampleBookListResponse();
            List<BookListResponse> books = Arrays.asList(sampleBook);
            Page<BookListResponse> bookPage = new PageImpl<>(books);

            when(adapter.getBooksByCategoryId(page, size, sort, categoryId))
                .thenReturn(new ResponseEntity<>(bookPage, HttpStatus.OK));

            // Act
            Page<BookListResponse> result = service.getBooksByCategoryId(page, size, sort,
                categoryId);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(adapter, times(1)).getBooksByCategoryId(page, size, sort, categoryId);
        }


        @Test
        @DisplayName("Adapter가 예외를 던지면 RuntimeException을 던져야 한다")
        void testGetBooksByCategoryIdAdapterException() {
            // Arrange
            int page = 0;
            int size = 10;
            String[] sort = {"name,asc"};
            Long categoryId = 1L;

            when(adapter.getBooksByCategoryId(page, size, sort, categoryId))
                .thenThrow(new RuntimeException("Adapter error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.getBooksByCategoryId(page, size, sort, categoryId));

            assertTrue(exception.getMessage().contains("카테고리로 책들을 조회하는데 실패하였습니다."));
            verify(adapter, times(1)).getBooksByCategoryId(page, size, sort, categoryId);
        }
    }

    @Nested
    @DisplayName("getBooksByTagId 테스트")
    class GetBooksByTagIdTests {

        @Test
        @DisplayName("API 호출이 성공하면 책 목록 페이지를 반환해야 한다")
        void testGetBooksByTagIdSuccess() {
            // Arrange
            int page = 0;
            int size = 10;
            String[] sort = {"name,asc"};
            Long tagId = 1L;

            BookListResponse sampleBook = createSampleBookListResponse();
            List<BookListResponse> books = Arrays.asList(sampleBook);
            Page<BookListResponse> bookPage = new PageImpl<>(books);

            when(adapter.getBooksByTagId(page, size, sort, tagId))
                .thenReturn(new ResponseEntity<>(bookPage, HttpStatus.OK));

            // Act
            Page<BookListResponse> result = service.getBooksByTagId(page, size, sort, tagId);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(adapter, times(1)).getBooksByTagId(page, size, sort, tagId);
        }


        @Test
        @DisplayName("Adapter가 예외를 던지면 RuntimeException을 던져야 한다")
        void testGetBooksByTagIdAdapterException() {
            // Arrange
            int page = 0;
            int size = 10;
            String[] sort = {"name,asc"};
            Long tagId = 1L;

            when(adapter.getBooksByTagId(page, size, sort, tagId))
                .thenThrow(new RuntimeException("Adapter error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.getBooksByTagId(page, size, sort, tagId));

            assertTrue(exception.getMessage().contains("태그로 책들을 조회하는데 실패하였습니다."));
            verify(adapter, times(1)).getBooksByTagId(page, size, sort, tagId);
        }
    }

    @Nested
    @DisplayName("getBookById 테스트")
    class GetBookByIdTests {

        @Test
        @DisplayName("API 호출이 성공하면 책 정보를 반환해야 한다")
        void testGetBookByIdSuccess() {
            // Arrange
            Long bookId = 1L;
            BookInfoResponse bookInfo = createSampleBookInfoResponse();

            when(adapter.getBookById(bookId))
                .thenReturn(new ResponseEntity<>(bookInfo, HttpStatus.OK));

            // Act
            BookInfoResponse result = service.getBookById(bookId);

            // Assert
            assertNotNull(result);
            assertEquals(bookId, result.getBookId());
            verify(adapter, times(1)).getBookById(bookId);
        }


        @Test
        @DisplayName("Adapter가 예외를 던지면 RuntimeException을 던져야 한다")
        void testGetBookByIdAdapterException() {
            // Arrange
            Long bookId = 1L;

            when(adapter.getBookById(bookId))
                .thenThrow(new RuntimeException("Adapter error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.getBookById(bookId));

            assertTrue(exception.getMessage().contains("책 아이디로 책을 조회하는데 실패하였습니다."));
            verify(adapter, times(1)).getBookById(bookId);
        }
    }

    @Nested
    @DisplayName("getCategoriesByBookId 테스트")
    class GetCategoriesByBookIdTests {

        @Test
        @DisplayName("API 호출이 성공하면 카테고리 목록을 반환해야 한다")
        void testGetCategoriesByBookIdSuccess() {
            // Arrange
            Long bookId = 1L;
            List<CategoryNameResponse> categories = Arrays.asList(
                new CategoryNameResponse(1L, "Fiction"),
                new CategoryNameResponse(2L, "Adventure")
            );

            when(adapter.getCategoriesByBookId(bookId))
                .thenReturn(new ResponseEntity<>(categories, HttpStatus.OK));

            // Act
            List<CategoryNameResponse> result = service.getCategoriesByBookId(bookId);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(adapter, times(1)).getCategoriesByBookId(bookId);
        }


        @Test
        @DisplayName("Adapter가 예외를 던지면 RuntimeException을 던져야 한다")
        void testGetCategoriesByBookIdAdapterException() {
            // Arrange
            Long bookId = 1L;

            when(adapter.getCategoriesByBookId(bookId))
                .thenThrow(new RuntimeException("Adapter error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.getCategoriesByBookId(bookId));

            assertTrue(exception.getMessage().contains("책 아이디로 카테고리들을 가져오는데 실패하였습니다."));
            verify(adapter, times(1)).getCategoriesByBookId(bookId);
        }
    }

    @Nested
    @DisplayName("getAllAvailableBooks 테스트")
    class GetAllAvailableBooksTests {

        @Test
        @DisplayName("API 호출이 성공하면 사용 가능한 책 목록 페이지를 반환해야 한다")
        void testGetAllAvailableBooksSuccess() {
            // Arrange
            int page = 0;
            int size = 10;
            String[] sort = {"name,asc"};

            BookListResponse sampleBook = createSampleBookListResponse();
            List<BookListResponse> books = Arrays.asList(sampleBook);
            Page<BookListResponse> bookPage = new PageImpl<>(books);

            when(adapter.getAllAvailableBooks(page, size, sort))
                .thenReturn(new ResponseEntity<>(bookPage, HttpStatus.OK));

            // Act
            Page<BookListResponse> result = service.getAllAvailableBooks(page, size, sort);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(adapter, times(1)).getAllAvailableBooks(page, size, sort);
        }


        @Test
        @DisplayName("Adapter가 예외를 던지면 RuntimeException을 던져야 한다")
        void testGetAllAvailableBooksAdapterException() {
            // Arrange
            int page = 0;
            int size = 10;
            String[] sort = {"name,asc"};

            when(adapter.getAllAvailableBooks(page, size, sort))
                .thenThrow(new RuntimeException("Adapter error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.getAllAvailableBooks(page, size, sort));

            assertTrue(exception.getMessage().contains("이용 가능한 책들을 가져오는데 실패하였습니다."));
            verify(adapter, times(1)).getAllAvailableBooks(page, size, sort);
        }
    }

    @Nested
    @DisplayName("getBooksByMemberId 테스트")
    class GetBooksByMemberIdTests {

        @Test
        @DisplayName("API 호출이 성공하면 회원의 책 목록 페이지를 반환해야 한다")
        void testGetBooksByMemberIdSuccess() {
            // Arrange
            int page = 0;
            int size = 10;
            String[] sort = {"name,asc"};

            BookListResponse sampleBook = createSampleBookListResponse();
            List<BookListResponse> books = Arrays.asList(sampleBook);
            Page<BookListResponse> bookPage = new PageImpl<>(books);

            when(adapter.getBooksByMemberId(page, size, sort))
                .thenReturn(new ResponseEntity<>(bookPage, HttpStatus.OK));

            // Act
            Page<BookListResponse> result = service.getBooksByMemberId(page, size, sort);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(adapter, times(1)).getBooksByMemberId(page, size, sort);
        }


        @Test
        @DisplayName("Adapter가 예외를 던지면 RuntimeException을 던져야 한다")
        void testGetBooksByMemberIdAdapterException() {
            // Arrange
            int page = 0;
            int size = 10;
            String[] sort = {"name,asc"};

            when(adapter.getBooksByMemberId(page, size, sort))
                .thenThrow(new RuntimeException("Adapter error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.getBooksByMemberId(page, size, sort));

            assertTrue(exception.getMessage().contains("회원 아이디로 책들을 가져오는데 실패하였습니다."));
            verify(adapter, times(1)).getBooksByMemberId(page, size, sort);
        }
    }

    @Nested
    @DisplayName("getLikedBooksIdForCurrentUser 테스트")
    class GetLikedBooksIdForCurrentUserTests {

        @Test
        @DisplayName("API 호출이 성공하면 좋아요한 책 ID 목록을 반환해야 한다")
        void testGetLikedBooksIdForCurrentUserSuccess() {
            // Arrange
            List<Long> likedBookIds = Arrays.asList(1L, 2L, 3L);

            when(adapter.getLikedBooksIdForCurrentUser())
                .thenReturn(new ResponseEntity<>(likedBookIds, HttpStatus.OK));

            // Act
            List<Long> result = service.getLikedBooksIdForCurrentUser();

            // Assert
            assertNotNull(result);
            assertEquals(3, result.size());
            verify(adapter, times(1)).getLikedBooksIdForCurrentUser();
        }


        @Test
        @DisplayName("Adapter가 예외를 던지면 RuntimeException을 던져야 한다")
        void testGetLikedBooksIdForCurrentUserAdapterException() {
            // Arrange
            when(adapter.getLikedBooksIdForCurrentUser())
                .thenThrow(new RuntimeException("Adapter error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.getLikedBooksIdForCurrentUser());

            assertTrue(exception.getMessage().contains("현재 유저가 좋아요를 누른 책의 정보를 가져오는데 실패하였습니다."));
            verify(adapter, times(1)).getLikedBooksIdForCurrentUser();
        }
    }

    @Nested
    @DisplayName("createBookLikeByMemberId 테스트")
    class CreateBookLikeByMemberIdTests {

        @Test
        @DisplayName("API 호출이 성공하면 BookLikeResponse를 반환해야 한다")
        void testCreateBookLikeByMemberIdSuccess() {
            // Arrange
            BookLikeRequest request = new BookLikeRequest(); // 적절한 필드 설정 필요
            BookLikeResponse response = new BookLikeResponse();
            setField(response, "bookId", 1L);
            setField(response, "likeCount", 11L);

            when(adapter.createBookLikeByMemberId(request))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.CREATED));

            // Act
            BookLikeResponse result = service.createBookLikeByMemberId(request);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getBookId());
            assertEquals(11L, result.getLikeCount());
            verify(adapter, times(1)).createBookLikeByMemberId(request);
        }


        @Test
        @DisplayName("Adapter가 예외를 던지면 RuntimeException을 던져야 한다")
        void testCreateBookLikeByMemberIdAdapterException() {
            // Arrange
            BookLikeRequest request = new BookLikeRequest(); // 적절한 필드 설정 필요

            when(adapter.createBookLikeByMemberId(request))
                .thenThrow(new RuntimeException("Adapter error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.createBookLikeByMemberId(request));

            assertTrue(exception.getMessage().contains("회원의 아이디로 좋아요를 누른 책을 만드는데 실패하였습니다."));
            verify(adapter, times(1)).createBookLikeByMemberId(request);
        }
    }

    @Nested
    @DisplayName("deleteBookLikeByMemberId 테스트")
    class DeleteBookLikeByMemberIdTests {

        @Test
        @DisplayName("API 호출이 성공하면 true를 반환해야 한다")
        void testDeleteBookLikeByMemberIdSuccess() {
            // Arrange
            Long bookId = 1L;

            when(adapter.deleteBookLikeByMemberId(bookId))
                .thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

            // Act
            Boolean result = service.deleteBookLikeByMemberId(bookId);

            // Assert
            assertNotNull(result);
            assertTrue(result);
            verify(adapter, times(1)).deleteBookLikeByMemberId(bookId);
        }


        @Test
        @DisplayName("Adapter가 예외를 던지면 RuntimeException을 던져야 한다")
        void testDeleteBookLikeByMemberIdAdapterException() {
            // Arrange
            Long bookId = 1L;

            when(adapter.deleteBookLikeByMemberId(bookId))
                .thenThrow(new RuntimeException("Adapter error"));

            // Act & Assert
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                service.deleteBookLikeByMemberId(bookId));

            assertTrue(exception.getMessage().contains("회원 아이디가 누른 좋아요를 삭제하는데 실패였습니다."));
            verify(adapter, times(1)).deleteBookLikeByMemberId(bookId);
        }
    }
}
