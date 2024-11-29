package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service.AuthorService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryAdminService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.service.PublisherMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.TagMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(BookMgmtController.class)
@AutoConfigureMockMvc(addFilters = false) // Security 필터 비활성화
class BookMgmtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookMgmtService bookMgmtService;

    @MockBean
    private CategoryAdminService categoryAdminService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private PublisherMgmtService publisherMgmtService;

    @MockBean
    private TagMgmtService tagMgmtService; // 추가

    private static final String ON_SALE = "판매 중";
    private static final String DELETE_BOOK = "삭제 도서";
    private static final String REDIRECT_ADDRESS = "/admin/books";

    @Test
    void testGetBooks() throws Exception {
        int page = 0;
        int size = 10;
        String[] sort = {"bookName:asc"};

        // Mock 데이터 설정
        Page<BookSearchResponse> booksPage = new PageImpl<>(List.of());

        Mockito.when(bookMgmtService.getAllBooks(page, size, sort)).thenReturn(booksPage);

        mockMvc.perform(get("/admin/books")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort[0]))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/book/books"));

        Mockito.verify(bookMgmtService).getAllBooks(page, size, sort);
    }



    @Test
    void testGetBooksWithExtraParams() throws Exception {
        // Arrange
        int page = 3;
        int size = 10;
        String[] sort = {"bookName:ASC"};

        // Mock 데이터 설정
        List<BookSearchResponse> booksContent = Arrays.asList(
            new BookSearchResponse(
                1L, "Book 1", "This is book 1", ON_SALE,
                Arrays.asList(new AuthorNameResponse(1L, "Author 1"))
            ),
            new BookSearchResponse(
                2L, "Book 2", "This is book 2", DELETE_BOOK,
                Arrays.asList(new AuthorNameResponse(2L, "Author 2"))
            )
        );

        Page<BookSearchResponse> booksPage = new PageImpl<>(booksContent, PageRequest.of(page, size, Sort.by("bookName").ascending()), booksContent.size());

        // Mock 서비스 호출 설정
        Mockito.when(bookMgmtService.getAllBooks(page, size, sort)).thenReturn(booksPage);

        // Act & Assert
        mockMvc.perform(get("/admin/books")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort[0]))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("books"))
            .andExpect(model().attributeExists("baseUrl"))
            .andExpect(model().attributeExists("extraParams"))
            .andExpect(model().attributeExists("sort"))
            .andExpect(model().attribute("books", booksPage))
            .andExpect(model().attribute("baseUrl", "/admin/books"))
            .andExpect(model().attribute("extraParams", "")) // extraParams가 빈 문자열로 설정된 경우 반영
            .andExpect(model().attribute("sort", sort[0]))
            .andExpect(view().name("admin/book/books"));

        // 서비스 호출 확인
        Mockito.verify(bookMgmtService).getAllBooks(page, size, sort);
    }

    @Test
    void testCreateBookForm() throws Exception {
        // Mock authors
        List<AuthorGetResponse> authors = Arrays.asList(
            new AuthorGetResponse(1L, "Author 1", "Author Info 1"),
            new AuthorGetResponse(2L, "Author 2", "Author Info 2")
        );
        Mockito.when(authorService.getAllAuthorList()).thenReturn(authors);

        // Mock categories
        List<CategoryInfoResponse> categories = Arrays.asList(
            new CategoryInfoResponse(1L, "Category 1", true, new ArrayList<>()),
            new CategoryInfoResponse(2L, "Category 2", true, new ArrayList<>())
        );
        ResponseEntity<List<CategoryInfoResponse>> categoriesResponse = ResponseEntity.ok(categories);
        Mockito.when(categoryAdminService.getAllCategoriesForAdmin()).thenReturn(categoriesResponse);

        // Mock publishers
        List<PublisherGetResponse> publishers = Arrays.asList(
            new PublisherGetResponse(1L, "Publisher 1", true),
            new PublisherGetResponse(2L, "Publisher 2", false)
        );
        Mockito.when(publisherMgmtService.getAllPublisherList()).thenReturn(publishers);

        // Mock tags
        List<TagInfoResponse> tags = Arrays.asList(
            new TagInfoResponse(1L, "Tag 1"),
            new TagInfoResponse(2L, "Tag 2")
        );
        Mockito.when(tagMgmtService.getAllTagList()).thenReturn(tags);

        // Perform test
        mockMvc.perform(get("/admin/books/create"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("bookSaveRequestMultipart"))
            .andExpect(model().attributeExists("authors"))
            .andExpect(model().attributeExists("categories"))
            .andExpect(model().attributeExists("publishers"))
            .andExpect(model().attributeExists("tags"))
            .andExpect(view().name("admin/book/createBook"));
    }

    @Test
    void testUpdateBookForm() throws Exception {
        // Arrange
        Long bookId = 1L;

        // Mock BookInfoResponse
        BookInfoResponse bookInfoResponse = new BookInfoResponse();
        bookInfoResponse.setBookId(bookId);
        bookInfoResponse.setBookName("Sample Book");
        bookInfoResponse.setBookIndex("Sample Index");
        bookInfoResponse.setBookInfo("This is a description of the book.");
        bookInfoResponse.setBookIsbn("1234567890123");
        bookInfoResponse.setPublicationDate(LocalDateTime.of(2022, 5, 20, 14, 0));
        bookInfoResponse.setRegularPrice(20000);
        bookInfoResponse.setSalePrice(18000);
        bookInfoResponse.setPacked(true);
        bookInfoResponse.setStock(50);
        bookInfoResponse.setBookThumbnailImageUrl("https://example.com/book-thumbnail.jpg");
        bookInfoResponse.setBookViewCount(0);
        bookInfoResponse.setBookDiscount(10);
        bookInfoResponse.setBookStatus(ON_SALE);

        // Mock Publisher
        PublisherNameResponse publisher = new PublisherNameResponse(1L, "Publisher Name");
        bookInfoResponse.setPublisher(publisher);

        // Mock Categories
        List<CategoryNameResponse> categories = List.of(
            new CategoryNameResponse(1L, "Fiction"),
            new CategoryNameResponse(2L, "Adventure")
        );
        bookInfoResponse.setCategories(categories);

        // Mock Tags
        List<TagInfoResponse> tags = List.of(
            new TagInfoResponse(1L, "Tag 1"),
            new TagInfoResponse(2L, "Tag 2")
        );
        bookInfoResponse.setTags(tags);

        // Mock Authors
        List<AuthorNameResponse> authors = List.of(
            new AuthorNameResponse(1L, "Author 1"),
            new AuthorNameResponse(2L, "Author 2")
        );
        bookInfoResponse.setAuthors(authors);


        // Mock BookUpdateRequestMultipart
        BookUpdateRequestMultipart bookUpdateRequestMultipart = new BookUpdateRequestMultipart();
        bookUpdateRequestMultipart.setBookId(bookId);
        bookUpdateRequestMultipart.setBookName("Updated Book");

        // Mock responses for services
        Mockito.when(bookMgmtService.getBookById(bookId)).thenReturn(bookInfoResponse);
        Mockito.when(bookMgmtService.setBookUpdateRequestMultipart(bookInfoResponse)).thenReturn(bookUpdateRequestMultipart);
        Mockito.when(publisherMgmtService.getAllPublisherList())
            .thenReturn(List.of(new PublisherGetResponse(1L, "Publisher 1", true)));
        Mockito.when(categoryAdminService.getAllCategoriesForAdmin())
            .thenReturn(ResponseEntity.ok(List.of(new CategoryInfoResponse(1L, "Category 1", true, new ArrayList<>()))));
        Mockito.when(tagMgmtService.getAllTagList())
            .thenReturn(List.of(new TagInfoResponse(1L, "Tag 1")));
        Mockito.when(authorService.getAllAuthorList())
            .thenReturn(List.of(new AuthorGetResponse(1L, "Author 1", "Author Info")));

        // Act & Assert
        mockMvc.perform(get("/admin/books/update/{book-id}", bookId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("bookInfoResponse"))
            .andExpect(model().attributeExists("bookUpdateRequestMultipart"))
            .andExpect(model().attributeExists("publishers"))
            .andExpect(model().attributeExists("existingCategories"))
            .andExpect(model().attributeExists("categories"))
            .andExpect(model().attributeExists("tags"))
            .andExpect(model().attributeExists("existingTags"))
            .andExpect(model().attributeExists("authors"))
            .andExpect(model().attributeExists("existingAuthors"))
            .andExpect(view().name("admin/book/updateBook"));

        // Verify service interactions
        Mockito.verify(bookMgmtService).getBookById(bookId);
        Mockito.verify(bookMgmtService).setBookUpdateRequestMultipart(bookInfoResponse);
        Mockito.verify(publisherMgmtService).getAllPublisherList();
        Mockito.verify(categoryAdminService).getAllCategoriesForAdmin();
        Mockito.verify(tagMgmtService).getAllTagList();
        Mockito.verify(authorService).getAllAuthorList();
    }

    @Test
    void testCreateBook_ValidRequest() throws Exception {
        // Arrange: Mock 데이터 생성
        BookSaveRequestMultipart bookSaveRequestMultipart = new BookSaveRequestMultipart();
        bookSaveRequestMultipart.setBookName("Test Book");
        bookSaveRequestMultipart.setBookIndex("Index 1");
        bookSaveRequestMultipart.setBookInfo("This is a test book.");
        bookSaveRequestMultipart.setBookIsbn("1234567890123");
        bookSaveRequestMultipart.setRegularPrice(15000);
        bookSaveRequestMultipart.setSalePrice(13500);
        bookSaveRequestMultipart.setIsPacked(true);
        bookSaveRequestMultipart.setStock(10);
        bookSaveRequestMultipart.setBookViewCount(100);
        bookSaveRequestMultipart.setBookDiscount(10);
        bookSaveRequestMultipart.setPublisherId(1L);
        bookSaveRequestMultipart.setBookStatus(ON_SALE);
        bookSaveRequestMultipart.setCategoryIdList(Arrays.asList(1L, 2L));
        bookSaveRequestMultipart.setAuthorIdList(Arrays.asList(3L, 4L));
        bookSaveRequestMultipart.setTagIdList(Arrays.asList(5L, 6L));
        MockMultipartFile mockThumbnail = new MockMultipartFile(
            "bookThumbnailImageUrl", "test-image.jpg", "image/jpeg", "dummy image data".getBytes()
        );
        bookSaveRequestMultipart.setBookThumbnailImageUrl(mockThumbnail);

        // Mock Service 동작 설정
        Mockito.doReturn(new MessageResponse())
            .when(bookMgmtService)
            .createBook(Mockito.any(BookSaveRequestMultipart.class), Mockito.any(MultipartFile.class));

        // Act & Assert: MockMvc로 요청 테스트
        mockMvc.perform(multipart("/admin/books/create")
                .file("bookThumbnailImageUrl", mockThumbnail.getBytes())
                .param("bookName", bookSaveRequestMultipart.getBookName())
                .param("bookIndex", bookSaveRequestMultipart.getBookIndex())
                .param("bookInfo", bookSaveRequestMultipart.getBookInfo())
                .param("bookIsbn", bookSaveRequestMultipart.getBookIsbn())
                .param("regularPrice", String.valueOf(bookSaveRequestMultipart.getRegularPrice()))
                .param("salePrice", String.valueOf(bookSaveRequestMultipart.getSalePrice()))
                .param("isPacked", String.valueOf(bookSaveRequestMultipart.getIsPacked()))
                .param("stock", String.valueOf(bookSaveRequestMultipart.getStock()))
                .param("bookViewCount", String.valueOf(bookSaveRequestMultipart.getBookViewCount()))
                .param("bookDiscount", String.valueOf(bookSaveRequestMultipart.getBookDiscount()))
                .param("bookStatus", String.valueOf(bookSaveRequestMultipart.getBookStatus()))
                .param("publisherId", String.valueOf(bookSaveRequestMultipart.getPublisherId()))
                .param("categoryIdList", "1", "2")
                .param("authorIdList", "3", "4")
                .param("tagIdList", "5", "6")
                .with(csrf()) // CSRF 토큰 설정
            )
            .andExpect(status().is3xxRedirection()) // 3xx 리다이렉트 확인
            .andExpect(redirectedUrl(REDIRECT_ADDRESS)); // 리다이렉트 주소 확인

        // Verify: 서비스 호출 확인
        Mockito.verify(bookMgmtService).createBook(Mockito.any(BookSaveRequestMultipart.class), Mockito.any(MultipartFile.class));
    }


    @Test
    void testUpdateBook_ValidRequestWithNewThumbnail() throws Exception {
        // Arrange: Mock 데이터 생성
        MockMultipartFile bookThumbnailImageUrlMultipartFile = new MockMultipartFile(
            "bookThumbnailImageUrlMultipartFile", "new-image.jpg", "image/jpeg", "Dummy Image Content".getBytes()
        );

        BookUpdateRequestMultipart requestMultipart = new BookUpdateRequestMultipart();
        requestMultipart.setBookId(1L);
        requestMultipart.setBookName("Updated Book Name");
        requestMultipart.setBookIndex("Updated Index");
        requestMultipart.setBookInfo("Updated Book Info");
        requestMultipart.setBookIsbn("1234567890123");
        requestMultipart.setRegularPrice(15000);
        requestMultipart.setSalePrice(13500);
        requestMultipart.setIsPacked(true);
        requestMultipart.setStock(10);
        requestMultipart.setBookViewCount(100);
        requestMultipart.setBookDiscount(10);
        requestMultipart.setPublisherId(1L);
        requestMultipart.setBookStatus(ON_SALE);
        requestMultipart.setCategoryIdList(List.of(1L, 2L));
        requestMultipart.setAuthorIdList(List.of(3L, 4L));
        requestMultipart.setTagIdList(List.of(5L, 6L));
        requestMultipart.setBookThumbnailImageUrl("old-image.jpg");

        // Mock 서비스 호출
        Mockito.when(bookMgmtService.updateBook(Mockito.any(BookUpdateRequestMultipart.class), Mockito.any(MultipartFile.class)))
            .thenReturn(new MessageResponse("Success"));

        // Act & Assert
        mockMvc.perform(multipart("/admin/books/update")
                .file(bookThumbnailImageUrlMultipartFile)
                .param("bookId", String.valueOf(requestMultipart.getBookId()))
                .param("bookName", requestMultipart.getBookName())
                .param("bookIndex", requestMultipart.getBookIndex())
                .param("bookInfo", requestMultipart.getBookInfo())
                .param("bookIsbn", requestMultipart.getBookIsbn())
                .param("regularPrice", String.valueOf(requestMultipart.getRegularPrice()))
                .param("salePrice", String.valueOf(requestMultipart.getSalePrice()))
                .param("isPacked", String.valueOf(requestMultipart.getIsPacked()))
                .param("stock", String.valueOf(requestMultipart.getStock()))
                .param("bookViewCount", String.valueOf(requestMultipart.getBookViewCount()))
                .param("bookDiscount", String.valueOf(requestMultipart.getBookDiscount()))
                .param("bookStatus", String.valueOf(requestMultipart.getBookStatus()))
                .param("publisherId", String.valueOf(requestMultipart.getPublisherId()))
                .param("categoryIdList", "1", "2")
                .param("authorIdList", "3", "4")
                .param("tagIdList", "5", "6")
                .with(csrf()) // CSRF 토큰 필요
            )
            .andExpect(status().is3xxRedirection()) // 리다이렉트 확인
            .andExpect(redirectedUrl(REDIRECT_ADDRESS)); // 리다이렉트 주소 확인

        // Verify: 서비스 호출 검증
        Mockito.verify(bookMgmtService).updateBook(Mockito.any(BookUpdateRequestMultipart.class), Mockito.any(MultipartFile.class));
    }

    @Test
    void testUpdateBook_ValidRequestWithExistingThumbnail() throws Exception {
        // Arrange: Mock 데이터 생성
        BookUpdateRequestMultipart requestMultipart = new BookUpdateRequestMultipart();
        requestMultipart.setBookId(1L);
        requestMultipart.setBookName("Updated Book Name");
        requestMultipart.setBookIndex("Updated Index");
        requestMultipart.setBookInfo("Updated Book Info");
        requestMultipart.setBookIsbn("1234567890123");
        requestMultipart.setRegularPrice(15000);
        requestMultipart.setSalePrice(13500);
        requestMultipart.setIsPacked(true);
        requestMultipart.setStock(10);
        requestMultipart.setBookViewCount(100);
        requestMultipart.setBookDiscount(10);
        requestMultipart.setPublisherId(1L);
        requestMultipart.setBookStatus(ON_SALE);
        requestMultipart.setCategoryIdList(List.of(1L, 2L));
        requestMultipart.setAuthorIdList(List.of(3L, 4L));
        requestMultipart.setTagIdList(List.of(5L, 6L));
        requestMultipart.setBookThumbnailImageUrl("old-image.jpg");

        // Mock 서비스 호출 반환값 설정
        Mockito.when(bookMgmtService.updateBook(Mockito.any(BookUpdateRequestMultipart.class), Mockito.isNull()))
            .thenReturn(new MessageResponse("Success"));

        // Act & Assert
        mockMvc.perform(multipart("/admin/books/update")
                .param("bookId", String.valueOf(requestMultipart.getBookId()))
                .param("bookName", requestMultipart.getBookName())
                .param("bookIndex", requestMultipart.getBookIndex())
                .param("bookInfo", requestMultipart.getBookInfo())
                .param("bookIsbn", requestMultipart.getBookIsbn())
                .param("regularPrice", String.valueOf(requestMultipart.getRegularPrice()))
                .param("salePrice", String.valueOf(requestMultipart.getSalePrice()))
                .param("isPacked", String.valueOf(requestMultipart.getIsPacked()))
                .param("stock", String.valueOf(requestMultipart.getStock()))
                .param("bookViewCount", String.valueOf(requestMultipart.getBookViewCount()))
                .param("bookDiscount", String.valueOf(requestMultipart.getBookDiscount()))
                .param("bookStatus", String.valueOf(requestMultipart.getBookStatus()))
                .param("publisherId", String.valueOf(requestMultipart.getPublisherId()))
                .param("categoryIdList", "1", "2")
                .param("authorIdList", "3", "4")
                .param("tagIdList", "5", "6")
                .param("bookThumbnailImageUrl", "old-image.jpg") // 기존 이미지 URL
                .with(csrf())
            )
            .andExpect(status().is3xxRedirection()) // 리다이렉트 상태 확인
            .andExpect(redirectedUrl(REDIRECT_ADDRESS)); // 리다이렉트 URL 확인

        // Verify: 서비스 호출 검증
        Mockito.verify(bookMgmtService).updateBook(Mockito.any(BookUpdateRequestMultipart.class), Mockito.isNull());
    }

    @Test
    void testDeleteBook_ValidRequest() throws Exception {
        // Arrange: Mock book ID
        Long bookId = 1L;

        // Mock 서비스 호출 설정
        Mockito.when(bookMgmtService.deleteBook(bookId)).thenReturn(new MessageResponse());

        // Act & Assert
        mockMvc.perform(post("/admin/books/delete/{book-id}", bookId)
                .with(csrf()) // CSRF 토큰 필요
            )
            .andExpect(status().is3xxRedirection()) // 리다이렉트 상태 확인
            .andExpect(redirectedUrl(REDIRECT_ADDRESS)); // 리다이렉트 URL 확인

        // Verify: 서비스 호출 검증
        Mockito.verify(bookMgmtService).deleteBook(bookId);
    }

}