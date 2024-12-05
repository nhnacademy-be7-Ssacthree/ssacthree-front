package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.DeliveryRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.PointSaveRuleCustomerService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.BookReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.service.ReviewService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(BookCustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookCustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCommonService bookCommonService;

    @MockBean
    private CategoryCommonService categoryCommonService;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private DeliveryRuleService deliveryRuleService;

    @MockBean
    private PointSaveRuleCustomerService pointSaveRuleCustomerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBooksByFilters_ShouldReturnBooksList() throws Exception {
        // Given
        Page<BookListResponse> mockBooks = new PageImpl<>(Collections.emptyList());
        when(bookCommonService.getAllAvailableBooks(0, 10, new String[]{"bookName:asc"}))
            .thenReturn(mockBooks);

        // When
        ResultActions resultActions = mockMvc.perform(get("/books")
            .param("page", "0")
            .param("size", "10")
            .param("sort", "bookName:asc")
            .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
            .andExpect(view().name("bookList"))
            .andExpect(model().attributeExists("books"))
            .andExpect(model().attributeExists("baseUrl"))
            .andExpect(model().attributeExists("extraParams"))
            .andExpect(model().attribute("sort", "bookName:asc"));
    }

    @Test
    void showHomeBook_ShouldReturnHomePageWithBannersAndBooks() throws Exception {
        // Given
        BookInfoResponse mockBanner1 = new BookInfoResponse(); // Mock Banner1 데이터 설정
        mockBanner1.setBookId(483L);

        BookListResponse mockBanner2 = new BookListResponse(); // Mock Banner2 데이터 설정
        Page<BookListResponse> mockBanner2Page = new PageImpl<>(
            Collections.singletonList(mockBanner2));

        Page<BookListResponse> mockAwardBooks = new PageImpl<>(Collections.emptyList());

        when(bookCommonService.getBookById(483L)).thenReturn(mockBanner1);
        when(bookCommonService.getAllAvailableBooks(0, 1, new String[]{"bookViewCount:desc"}))
            .thenReturn(mockBanner2Page);
        when(bookCommonService.getBooksByAuthorId(0, 10, new String[]{"bookName"}, 336L))
            .thenReturn(mockAwardBooks);

        // When
        ResultActions resultActions = mockMvc.perform(get("/")
            .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attributeExists("banner1"))
            .andExpect(model().attributeExists("banner2"))
            .andExpect(model().attributeExists("books"))
            .andExpect(model().attributeExists("currentPage"))
            .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    void showBook_ShouldReturnBookDetails() throws Exception {
        // Given
        Long bookId = 1L;

        // Mock BookInfoResponse
        BookInfoResponse mockBookInfo = BookInfoResponse.builder()
            .bookId(bookId)
            .bookName("Test Book")
            .bookInfo("This is a test book description.")
            .bookIsbn("1234567890123")
            .publicationDate(LocalDateTime.now())
            .regularPrice(20000)
            .salePrice(18000)
            .stock(50)
            .bookViewCount(120)
            .bookStatus("Available")
            .build();
        mockBookInfo.setPublisher(new PublisherNameResponse(1L, "test"));
        // Mock CategoryNameResponse
        CategoryNameResponse mockCategoryName = new CategoryNameResponse(10L, "Fiction");
        List<CategoryNameResponse> mockCategories = List.of(mockCategoryName);

        // Mock CategoryInfoResponse
        CategoryInfoResponse mockCategoryInfo = new CategoryInfoResponse(10L, "Fiction", true,
            new ArrayList<>());
        List<List<CategoryInfoResponse>> mockCategoryPaths = List.of(List.of(mockCategoryInfo));

        // Mock DeliveryRuleGetResponse
        DeliveryRuleGetResponse mockDeliveryRule = new DeliveryRuleGetResponse();

        // Mock PointSaveRuleInfoResponse
        PointSaveRuleInfoResponse mockPointSaveRule = new PointSaveRuleInfoResponse();

        // Mock BookReviewResponse
        BookReviewResponse mockReview = new BookReviewResponse();
        List<BookReviewResponse> mockReviewList = List.of(mockReview);
        Page<BookReviewResponse> mockReviews = new PageImpl<>(mockReviewList);

        // Mock Service Responses
        when(bookCommonService.getBookById(bookId)).thenReturn(mockBookInfo);
        when(bookCommonService.getCategoriesByBookId(bookId)).thenReturn(mockCategories);
        when(categoryCommonService.getCategoryPath(mockCategoryName.getCategoryId())).thenReturn(
            mockCategoryPaths.get(0));
        when(deliveryRuleService.getCurrentDeliveryRule()).thenReturn(mockDeliveryRule);
        when(pointSaveRuleCustomerService.getBookPointSaveRule()).thenReturn(mockPointSaveRule);
        when(reviewService.getReviewsByBookId(0, 10, new String[]{"reviewCreatedAt:desc"}, bookId))
            .thenReturn(mockReviews);

        // When
        ResultActions resultActions = mockMvc.perform(get("/books/{book-id}", bookId)
            .param("page", "0")
            .param("size", "10")
            .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
            .andExpect(view().name("bookDetails"))
            .andExpect(model().attributeExists("book"))
            .andExpect(model().attributeExists("deliveryRule"))
            .andExpect(model().attributeExists("bookPointSaveRule"))
            .andExpect(model().attributeExists("reviews"))
            .andExpect(model().attributeExists("paging"))
            .andExpect(model().attributeExists("baseUrl"))
            .andExpect(model().attributeExists("categoryPaths"));
    }


}
