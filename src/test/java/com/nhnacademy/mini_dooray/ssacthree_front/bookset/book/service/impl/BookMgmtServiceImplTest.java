package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter.BookMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.exception.BookFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.image.adapter.ImageUploadAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookMgmtServiceImplTest {

    @Mock
    private BookMgmtAdapter bookMgmtAdapter;

    @Mock
    private ImageUploadAdapter imageUploadAdapter;

    @InjectMocks
    private BookMgmtServiceImpl bookMgmtService;

    private static final String[] SORT = {"bookName:asc"};


    private static final String IMAGE_PATH = "/ssacthree/bookImage/";
    private static final String BOOK_SEARCH_ERROR = "책 정보 조회에 실패했습니다.";
    private static final String BOOK_CREATE_ERROR = "책 정보 생성에 실패했습니다.";
    private static final String BOOK_UPDATE_ERROR = "책 정보 수정에 실패했습니다.";
    private static final String BOOK_DELETE_ERROR = "책 정보 삭제에 실패했습니다.";
    private static final String ON_SALE = "판매 중";
    private static final String DELETE_BOOK = "삭제 도서";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooks_ShouldReturnPage_WhenResponseIsSuccessful() {
        // Given
        int page = 0, size = 10;
        List<BookSearchResponse> books = List.of(
            new BookSearchResponse(1L, "Book 1", "Info 1", ON_SALE, null),
            new BookSearchResponse(2L, "Book 2", "Info 2", DELETE_BOOK, null)
        );

        Page<BookSearchResponse> expectedPage = new PageImpl<>(books);
        ResponseEntity<Page<BookSearchResponse>> responseEntity = ResponseEntity.ok(expectedPage);

        Mockito.when(bookMgmtAdapter.getAllBooks(page, size, SORT)).thenReturn(responseEntity);

        // When
        Page<BookSearchResponse> result = bookMgmtService.getAllBooks(page, size, SORT);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Book 1", result.getContent().get(0).getBookName());
        Mockito.verify(bookMgmtAdapter).getAllBooks(page, size, SORT);
    }

    @Test
    void getAllBooks_ShouldThrowBookFailedException_WhenResponseIsUnsuccessful() {
        // Given
        int page = 0, size = 10;
        ResponseEntity<Page<BookSearchResponse>> responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        Mockito.when(bookMgmtAdapter.getAllBooks(page, size, SORT)).thenReturn(responseEntity);

        // When & Then
        BookFailedException exception = assertThrows(BookFailedException.class,
            () -> bookMgmtService.getAllBooks(page, size, SORT));
        assertEquals(BOOK_SEARCH_ERROR, exception.getMessage());
        Mockito.verify(bookMgmtAdapter).getAllBooks(page, size, SORT);
    }

    @Test
    void getAllBooks_ShouldThrowBookFailedException_WhenHttpClientErrorOccurs() {
        // Given
        int page = 0, size = 10;

        Mockito.when(bookMgmtAdapter.getAllBooks(page, size, SORT))
            .thenThrow(new BookFailedException(BOOK_SEARCH_ERROR));

        // When & Then
        BookFailedException exception = assertThrows(BookFailedException.class,
            () -> bookMgmtService.getAllBooks(page, size, SORT));
        assertEquals(BOOK_SEARCH_ERROR, exception.getMessage());
        Mockito.verify(bookMgmtAdapter).getAllBooks(page, size, SORT);
    }

    @Test
    void getAllBooks_ShouldThrowBookFailedException_WhenHttpServerErrorOccurs() {
        // Given
        int page = 0, size = 10;

        Mockito.when(bookMgmtAdapter.getAllBooks(page, size, SORT))
            .thenThrow(new BookFailedException(BOOK_SEARCH_ERROR));

        // When & Then
        BookFailedException exception = assertThrows(BookFailedException.class,
            () -> bookMgmtService.getAllBooks(page, size, SORT));
        assertEquals(BOOK_SEARCH_ERROR, exception.getMessage());
        Mockito.verify(bookMgmtAdapter).getAllBooks(page, size, SORT);
    }

    @Test
    void createBook_ShouldReturnNull_WhenSuccessful() {
        // Given
        MultipartFile thumbnailFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[]{1, 2, 3});
        String expectedImageUrl = "http://example.com/images/image.jpg";

        BookSaveRequestMultipart bookSaveRequestMultipart = new BookSaveRequestMultipart();
        bookSaveRequestMultipart.setBookName("Sample Book");
        bookSaveRequestMultipart.setBookIsbn("1234567890");

        BookSaveRequest expectedBookSaveRequest = new BookSaveRequest();
        expectedBookSaveRequest.setBookName("Sample Book");
        expectedBookSaveRequest.setBookIsbn("1234567890");
        expectedBookSaveRequest.setBookThumbnailImageUrl(expectedImageUrl);

        // Mock image upload success
        Mockito.when(imageUploadAdapter.uploadImage(thumbnailFile, IMAGE_PATH)).thenReturn(expectedImageUrl);

        // Mock book creation success
        ResponseEntity<MessageResponse> mockResponse = new ResponseEntity<>(null, HttpStatus.CREATED);
        Mockito.when(bookMgmtAdapter.createBook(Mockito.any(BookSaveRequest.class))).thenReturn(mockResponse);

        // When
        MessageResponse actualResponse = bookMgmtService.createBook(bookSaveRequestMultipart, thumbnailFile);

        // Then
        assertNull(actualResponse); // 성공 시 응답 메시지가 없는 경우

        Mockito.verify(imageUploadAdapter).uploadImage(thumbnailFile, IMAGE_PATH);
        Mockito.verify(bookMgmtAdapter).createBook(Mockito.any(BookSaveRequest.class));
    }


    @Test
    void createBook_ShouldThrowException_WhenImageUploadFails() {
        // Given
        MultipartFile thumbnailFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[]{1, 2, 3});
        BookSaveRequestMultipart bookSaveRequestMultipart = new BookSaveRequestMultipart();

        Mockito.when(imageUploadAdapter.uploadImage(thumbnailFile, IMAGE_PATH))
            .thenThrow(new RuntimeException("Image upload failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> bookMgmtService.createBook(bookSaveRequestMultipart, thumbnailFile));
        assertEquals("Image upload failed", exception.getMessage());

        Mockito.verify(imageUploadAdapter).uploadImage(thumbnailFile, IMAGE_PATH);
        Mockito.verifyNoInteractions(bookMgmtAdapter); // 도서 생성 호출되지 않아야 함
    }

    @Test
    void createBook_ShouldThrowException_WhenBookCreationFails() {
        // Given
        MultipartFile thumbnailFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[]{1, 2, 3});
        String expectedImageUrl = "http://example.com/images/image.jpg";

        BookSaveRequestMultipart bookSaveRequestMultipart = new BookSaveRequestMultipart();

        // Mock to return expected image URL
        Mockito.when(imageUploadAdapter.uploadImage(thumbnailFile, IMAGE_PATH)).thenReturn(expectedImageUrl);

        // Mock to throw exception on book creation
        Mockito.when(bookMgmtAdapter.createBook(Mockito.any(BookSaveRequest.class)))
            .thenThrow(new BookFailedException(BOOK_CREATE_ERROR));

        // When & Then
        BookFailedException exception = assertThrows(BookFailedException.class,
            () -> bookMgmtService.createBook(bookSaveRequestMultipart, thumbnailFile));

        assertTrue(exception.getMessage().contains(BOOK_CREATE_ERROR));

        // Verify interactions
        Mockito.verify(imageUploadAdapter).uploadImage(thumbnailFile, IMAGE_PATH);
        Mockito.verify(bookMgmtAdapter).createBook(Mockito.argThat(request ->
            expectedImageUrl.equals(request.getBookThumbnailImageUrl())
        ));
    }

    @Test
    void testUpdateBook_WithExistingUrl() {
        // Arrange
        BookUpdateRequestMultipart requestMultipart = new BookUpdateRequestMultipart();
        requestMultipart.setBookId(1L);
        requestMultipart.setBookThumbnailImageUrl("https://example.com/old-thumbnail.jpg");

        MultipartFile mockFile = null; // No new file uploaded

        MessageResponse mockResponse = new MessageResponse("Success");
        ResponseEntity<MessageResponse> responseEntity = ResponseEntity.ok(mockResponse);

        Mockito.when(bookMgmtAdapter.updateBook(Mockito.any(BookUpdateRequest.class)))
            .thenReturn(responseEntity);

        // Act
        MessageResponse response = bookMgmtService.updateBook(requestMultipart, mockFile);

        // Assert
        Assertions.assertEquals("Success", response.getMessage());
        Mockito.verify(bookMgmtAdapter, Mockito.times(1))
            .updateBook(Mockito.argThat(request ->
                "https://example.com/old-thumbnail.jpg".equals(request.getBookThumbnailImageUrl())
            ));
        Mockito.verifyNoInteractions(imageUploadAdapter);
    }

    @Test
    void testUpdateBook_WithNewFile() {
        // Arrange
        BookUpdateRequestMultipart requestMultipart = new BookUpdateRequestMultipart();
        requestMultipart.setBookId(1L);

        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockFile.isEmpty()).thenReturn(false);
        Mockito.when(mockFile.getOriginalFilename()).thenReturn("new-thumbnail.jpg");

        String newImageUrl = "https://example.com/new-thumbnail.jpg";
        Mockito.when(imageUploadAdapter.uploadImage(Mockito.eq(mockFile), Mockito.anyString()))
            .thenReturn(newImageUrl);

        MessageResponse mockResponse = new MessageResponse("Success");
        ResponseEntity<MessageResponse> responseEntity = ResponseEntity.ok(mockResponse);

        Mockito.when(bookMgmtAdapter.updateBook(Mockito.any(BookUpdateRequest.class)))
            .thenReturn(responseEntity);

        // Act
        MessageResponse response = bookMgmtService.updateBook(requestMultipart, mockFile);

        // Assert
        Assertions.assertEquals("Success", response.getMessage());
        Mockito.verify(bookMgmtAdapter, Mockito.times(1))
            .updateBook(Mockito.argThat(request ->
                newImageUrl.equals(request.getBookThumbnailImageUrl())
            ));
        Mockito.verify(imageUploadAdapter, Mockito.times(1))
            .uploadImage(Mockito.eq(mockFile), Mockito.anyString());
    }

    @Test
    void testUpdateBook_WithApiError() {
        // Arrange
        BookUpdateRequestMultipart requestMultipart = new BookUpdateRequestMultipart();
        requestMultipart.setBookId(1L);
        requestMultipart.setBookThumbnailImageUrl("https://example.com/old-thumbnail.jpg");

        MultipartFile mockFile = null; // No new file uploaded

        Mockito.when(bookMgmtAdapter.updateBook(Mockito.any(BookUpdateRequest.class)))
            .thenThrow(new BookFailedException(BOOK_UPDATE_ERROR));

        // Act & Assert
        Assertions.assertThrows(BookFailedException.class, () ->
            bookMgmtService.updateBook(requestMultipart, mockFile)
        );

        Mockito.verify(bookMgmtAdapter, Mockito.times(1))
            .updateBook(Mockito.any(BookUpdateRequest.class));
    }

    @Test
    void testDeleteBook_Success() {
        // Arrange
        Long bookId = 1L;
        MessageResponse mockResponse = new MessageResponse("Book deleted successfully");
        ResponseEntity<MessageResponse> responseEntity = ResponseEntity.ok(mockResponse);

        Mockito.when(bookMgmtAdapter.deleteBook(bookId)).thenReturn(responseEntity);

        // Act
        MessageResponse response = bookMgmtService.deleteBook(bookId);

        // Assert
        Assertions.assertEquals("Book deleted successfully", response.getMessage());
        Mockito.verify(bookMgmtAdapter, Mockito.times(1)).deleteBook(bookId);
    }

    @Test
    void testDeleteBook_ApiError() {
        // Arrange
        Long bookId = 1L;

        Mockito.when(bookMgmtAdapter.deleteBook(bookId))
            .thenThrow(new BookFailedException(BOOK_DELETE_ERROR));

        // Act & Assert
        Assertions.assertThrows(BookFailedException.class, () -> {
            bookMgmtService.deleteBook(bookId);
        });

        Mockito.verify(bookMgmtAdapter, Mockito.times(1)).deleteBook(bookId);
    }

    @Test
    void testDeleteBook_ServerError() {
        // Arrange
        Long bookId = 1L;

        Mockito.when(bookMgmtAdapter.deleteBook(bookId))
            .thenThrow(new BookFailedException(BOOK_DELETE_ERROR));

        // Act & Assert
        Assertions.assertThrows(BookFailedException.class, () -> {
            bookMgmtService.deleteBook(bookId);
        });

        Mockito.verify(bookMgmtAdapter, Mockito.times(1)).deleteBook(bookId);
    }

    @Test
    void testDeleteBook_Non2xxResponse() {
        // Arrange
        Long bookId = 1L;
        ResponseEntity<MessageResponse> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new MessageResponse("Book deletion failed"));

        Mockito.when(bookMgmtAdapter.deleteBook(bookId)).thenReturn(responseEntity);

        // Act & Assert
        Assertions.assertThrows(IllegalStateException.class, () -> {
            bookMgmtService.deleteBook(bookId);
        });

        Mockito.verify(bookMgmtAdapter, Mockito.times(1)).deleteBook(bookId);
    }

    @Test
    void testGetBookById_Success() {
        // Arrange
        Long bookId = 1L;
        BookInfoResponse mockResponse = new BookInfoResponse();
        mockResponse.setBookId(bookId);
        mockResponse.setBookName("Test Book");

        ResponseEntity<BookInfoResponse> responseEntity = ResponseEntity.ok(mockResponse);

        Mockito.when(bookMgmtAdapter.getBookByBookId(bookId)).thenReturn(responseEntity);

        // Act
        BookInfoResponse response = bookMgmtService.getBookById(bookId);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(bookId, response.getBookId());
        Assertions.assertEquals("Test Book", response.getBookName());
        Mockito.verify(bookMgmtAdapter, Mockito.times(1)).getBookByBookId(bookId);
    }

    @Test
    void testGetBookById_ClientError() {
        // Arrange
        Long bookId = 1L;

        Mockito.when(bookMgmtAdapter.getBookByBookId(bookId))
            .thenThrow(new BookFailedException(BOOK_SEARCH_ERROR));

        // Act & Assert
        Assertions.assertThrows(BookFailedException.class, () -> {
            bookMgmtService.getBookById(bookId);
        });

        Mockito.verify(bookMgmtAdapter, Mockito.times(1)).getBookByBookId(bookId);
    }

    @Test
    void testGetBookById_ServerError() {
        // Arrange
        Long bookId = 1L;

        Mockito.when(bookMgmtAdapter.getBookByBookId(bookId))
            .thenThrow(new BookFailedException(BOOK_SEARCH_ERROR));

        // Act & Assert
        Assertions.assertThrows(BookFailedException.class, () -> {
            bookMgmtService.getBookById(bookId);
        });

        Mockito.verify(bookMgmtAdapter, Mockito.times(1)).getBookByBookId(bookId);
    }

    @Test
    void testGetBookById_Non2xxResponse() {
        // Arrange
        Long bookId = 1L;
        ResponseEntity<BookInfoResponse> responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        Mockito.when(bookMgmtAdapter.getBookByBookId(bookId)).thenReturn(responseEntity);

        // Act & Assert
        Assertions.assertThrows(BookFailedException.class, () -> {
            bookMgmtService.getBookById(bookId);
        });

        Mockito.verify(bookMgmtAdapter, Mockito.times(1)).getBookByBookId(bookId);
    }
    @Test
    void testSetBookUpdateRequestMultipart() {
        // Arrange
        BookInfoResponse bookInfoResponse = new BookInfoResponse();
        bookInfoResponse.setBookId(1L);
        bookInfoResponse.setBookName("Test Book");
        bookInfoResponse.setBookIndex("Index Content");
        bookInfoResponse.setBookInfo("This is a test book.");
        bookInfoResponse.setBookIsbn("1234567890123");
        bookInfoResponse.setPublicationDate(LocalDateTime.of(2023, 11, 1, 10, 0));
        bookInfoResponse.setRegularPrice(20000);
        bookInfoResponse.setSalePrice(15000);
        bookInfoResponse.setPacked(true);
        bookInfoResponse.setStock(50);
        bookInfoResponse.setBookThumbnailImageUrl("https://example.com/image.jpg");
        bookInfoResponse.setBookDiscount(25);
        bookInfoResponse.setBookStatus(ON_SALE);

        PublisherNameResponse publisher = new PublisherNameResponse();
        publisher.setPublisherId(100L);
        bookInfoResponse.setPublisher(publisher);

        CategoryNameResponse category1 = new CategoryNameResponse(1L, "Category1");
        CategoryNameResponse category2 = new CategoryNameResponse(2L, "Category2");
        bookInfoResponse.setCategories(List.of(category1, category2));

        TagInfoResponse tag1 = new TagInfoResponse(1L, "Tag1");
        TagInfoResponse tag2 = new TagInfoResponse(2L, "Tag2");
        bookInfoResponse.setTags(List.of(tag1, tag2));

        AuthorNameResponse author1 = new AuthorNameResponse(1L, "Author1");
        AuthorNameResponse author2 = new AuthorNameResponse(2L, "Author2");
        bookInfoResponse.setAuthors(List.of(author1, author2));

        // Act
        BookUpdateRequestMultipart result = bookMgmtService.setBookUpdateRequestMultipart(bookInfoResponse);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookInfoResponse.getBookId(), result.getBookId());
        Assertions.assertEquals(bookInfoResponse.getBookName(), result.getBookName());
        Assertions.assertEquals(bookInfoResponse.getBookIndex(), result.getBookIndex());
        Assertions.assertEquals(bookInfoResponse.getBookInfo(), result.getBookInfo());
        Assertions.assertEquals(bookInfoResponse.getBookIsbn(), result.getBookIsbn());
        Assertions.assertEquals(bookInfoResponse.getPublicationDate().toLocalDate(), result.getPublicationDate());
        Assertions.assertEquals(bookInfoResponse.getRegularPrice(), result.getRegularPrice());
        Assertions.assertEquals(bookInfoResponse.getSalePrice(), result.getSalePrice());
        Assertions.assertEquals(bookInfoResponse.isPacked(), result.getIsPacked());
        Assertions.assertEquals(bookInfoResponse.getStock(), result.getStock());
        Assertions.assertEquals(bookInfoResponse.getBookThumbnailImageUrl(), result.getBookThumbnailImageUrl());
        Assertions.assertEquals(bookInfoResponse.getBookDiscount(), result.getBookDiscount());
        Assertions.assertEquals(bookInfoResponse.getBookStatus(), result.getBookStatus());
        Assertions.assertEquals(bookInfoResponse.getPublisher().getPublisherId(), result.getPublisherId());

        List<Long> expectedCategoryIds = bookInfoResponse.getCategories().stream()
            .map(CategoryNameResponse::getCategoryId)
            .collect(Collectors.toList());
        Assertions.assertEquals(expectedCategoryIds, result.getCategoryIdList());

        List<Long> expectedTagIds = bookInfoResponse.getTags().stream()
            .map(TagInfoResponse::getTagId)
            .collect(Collectors.toList());
        Assertions.assertEquals(expectedTagIds, result.getTagIdList());

        List<Long> expectedAuthorIds = bookInfoResponse.getAuthors().stream()
            .map(AuthorNameResponse::getAuthorId)
            .collect(Collectors.toList());
        Assertions.assertEquals(expectedAuthorIds, result.getAuthorIdList());
    }

    @Test
    void testConvertToBookSaveRequest() {
        // Arrange
        BookSaveRequestMultipart bookSaveRequestMultipart = new BookSaveRequestMultipart();
        bookSaveRequestMultipart.setBookId(1L);
        bookSaveRequestMultipart.setBookName("Test Book");
        bookSaveRequestMultipart.setBookIndex("Index Content");
        bookSaveRequestMultipart.setBookInfo("This is a test book.");
        bookSaveRequestMultipart.setBookIsbn("1234567890123");
        bookSaveRequestMultipart.setPublicationDate(LocalDate.of(2023, 11, 1));
        bookSaveRequestMultipart.setRegularPrice(20000);
        bookSaveRequestMultipart.setSalePrice(15000);
        bookSaveRequestMultipart.setIsPacked(true);
        bookSaveRequestMultipart.setStock(50);

        String imageUrl = "https://example.com/image.jpg";

        bookSaveRequestMultipart.setBookViewCount(100);
        bookSaveRequestMultipart.setBookDiscount(25);
        bookSaveRequestMultipart.setBookStatus(ON_SALE);
        bookSaveRequestMultipart.setPublisherId(100L);
        bookSaveRequestMultipart.setCategoryIdList(List.of(1L, 2L));
        bookSaveRequestMultipart.setAuthorIdList(List.of(3L, 4L));
        bookSaveRequestMultipart.setTagIdList(List.of(5L, 6L));

        // Act
        BookSaveRequest result = bookMgmtService.convertToBookSaveRequest(bookSaveRequestMultipart, imageUrl);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(imageUrl, result.getBookThumbnailImageUrl());
        Assertions.assertEquals(bookSaveRequestMultipart.getBookId(), result.getBookId());
        Assertions.assertEquals(bookSaveRequestMultipart.getBookName(), result.getBookName());
        Assertions.assertEquals(bookSaveRequestMultipart.getBookIndex(), result.getBookIndex());
        Assertions.assertEquals(bookSaveRequestMultipart.getBookInfo(), result.getBookInfo());
        Assertions.assertEquals(bookSaveRequestMultipart.getBookIsbn(), result.getBookIsbn());
        Assertions.assertEquals(bookSaveRequestMultipart.getPublicationDate(), result.getPublicationDate());
        Assertions.assertEquals(bookSaveRequestMultipart.getRegularPrice(), result.getRegularPrice());
        Assertions.assertEquals(bookSaveRequestMultipart.getSalePrice(), result.getSalePrice());
        Assertions.assertEquals(bookSaveRequestMultipart.getIsPacked(), result.getIsPacked());
        Assertions.assertEquals(bookSaveRequestMultipart.getStock(), result.getStock());
        Assertions.assertEquals(bookSaveRequestMultipart.getBookViewCount(), result.getBookViewCount());
        Assertions.assertEquals(bookSaveRequestMultipart.getBookDiscount(), result.getBookDiscount());
        Assertions.assertEquals(bookSaveRequestMultipart.getBookStatus(), result.getBookStatus());
        Assertions.assertEquals(bookSaveRequestMultipart.getPublisherId(), result.getPublisherId());
        Assertions.assertEquals(bookSaveRequestMultipart.getCategoryIdList(), result.getCategoryIdList());
        Assertions.assertEquals(bookSaveRequestMultipart.getAuthorIdList(), result.getAuthorIdList());
        Assertions.assertEquals(bookSaveRequestMultipart.getTagIdList(), result.getTagIdList());
    }

    @Test
    void testConvertToBookSaveRequest_WithValidImageUrl() {
        // Arrange
        BookSaveRequestMultipart bookSaveRequestMultipart = new BookSaveRequestMultipart();
        bookSaveRequestMultipart.setBookThumbnailImageUrl(new MockMultipartFile(
            "bookThumbnailImageUrl",
            "original-image.jpg",
            "image/jpeg",
            "image data".getBytes()
        ));

        String imageUrl = "http://example.com/images/original-image.jpg"; // 항상 유효한 URL

        // Act
        BookSaveRequest result = bookMgmtService.convertToBookSaveRequest(bookSaveRequestMultipart, imageUrl);

        // Assert
        Assertions.assertEquals(imageUrl, result.getBookThumbnailImageUrl());
    }


    @Test
    void testConvertToBookUpdateRequest() {
        // Arrange
        BookUpdateRequestMultipart bookUpdateRequestMultipart = new BookUpdateRequestMultipart();
        bookUpdateRequestMultipart.setBookId(1L);
        bookUpdateRequestMultipart.setBookName("Test Book");
        bookUpdateRequestMultipart.setBookIndex("Index Content");
        bookUpdateRequestMultipart.setBookInfo("This is a test book.");
        bookUpdateRequestMultipart.setBookIsbn("1234567890123");
        bookUpdateRequestMultipart.setPublicationDate(LocalDate.of(2023, 11, 1));
        bookUpdateRequestMultipart.setRegularPrice(20000);
        bookUpdateRequestMultipart.setSalePrice(15000);
        bookUpdateRequestMultipart.setIsPacked(true);
        bookUpdateRequestMultipart.setStock(50);
        bookUpdateRequestMultipart.setBookThumbnailImageUrl("https://example.com/original-image.jpg");
        bookUpdateRequestMultipart.setBookDiscount(25);
        bookUpdateRequestMultipart.setBookStatus(ON_SALE);
        bookUpdateRequestMultipart.setPublisherId(100L);
        bookUpdateRequestMultipart.setCategoryIdList(List.of(1L, 2L));
        bookUpdateRequestMultipart.setTagIdList(List.of(3L, 4L));
        bookUpdateRequestMultipart.setAuthorIdList(List.of(5L, 6L));

        String newImageUrl = "https://example.com/new-image.jpg";

        // Act
        BookUpdateRequest result = bookMgmtService.convertToBookUpdateRequest(bookUpdateRequestMultipart, newImageUrl);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookUpdateRequestMultipart.getBookId(), result.getBookId());
        Assertions.assertEquals(bookUpdateRequestMultipart.getBookName(), result.getBookName());
        Assertions.assertEquals(bookUpdateRequestMultipart.getBookIndex(), result.getBookIndex());
        Assertions.assertEquals(bookUpdateRequestMultipart.getBookInfo(), result.getBookInfo());
        Assertions.assertEquals(bookUpdateRequestMultipart.getBookIsbn(), result.getBookIsbn());
        Assertions.assertEquals(bookUpdateRequestMultipart.getPublicationDate(), result.getPublicationDate());
        Assertions.assertEquals(bookUpdateRequestMultipart.getRegularPrice(), result.getRegularPrice());
        Assertions.assertEquals(bookUpdateRequestMultipart.getSalePrice(), result.getSalePrice());
        Assertions.assertEquals(bookUpdateRequestMultipart.getIsPacked(), result.getIsPacked());
        Assertions.assertEquals(bookUpdateRequestMultipart.getStock(), result.getStock());
        Assertions.assertEquals(newImageUrl, result.getBookThumbnailImageUrl());
        Assertions.assertEquals(bookUpdateRequestMultipart.getBookDiscount(), result.getBookDiscount());
        Assertions.assertEquals(bookUpdateRequestMultipart.getBookStatus(), result.getBookStatus());
        Assertions.assertEquals(bookUpdateRequestMultipart.getPublisherId(), result.getPublisherId());
        Assertions.assertEquals(bookUpdateRequestMultipart.getCategoryIdList(), result.getCategoryIdList());
        Assertions.assertEquals(bookUpdateRequestMultipart.getTagIdList(), result.getTagIdList());
        Assertions.assertEquals(bookUpdateRequestMultipart.getAuthorIdList(), result.getAuthorIdList());
    }

    @Test
    void testConvertToBookUpdateRequest_WithNullImageUrl() {
        // Arrange
        BookUpdateRequestMultipart bookUpdateRequestMultipart = new BookUpdateRequestMultipart();
        bookUpdateRequestMultipart.setBookThumbnailImageUrl("https://example.com/original-image.jpg");

        // Act
        BookUpdateRequest result = bookMgmtService.convertToBookUpdateRequest(bookUpdateRequestMultipart, null);

        // Assert
        Assertions.assertEquals(bookUpdateRequestMultipart.getBookThumbnailImageUrl(), result.getBookThumbnailImageUrl());
    }

}
