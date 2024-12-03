package com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.controller;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service.AuthorService;
import java.util.List;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(AuthorController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    void testGetAuthors_Success() throws Exception {
        // Arrange
        int page = 0;
        int size = 10;
        String[] sort = {"authorName:asc"};

        List<AuthorGetResponse> authors = List.of(
            new AuthorGetResponse(1L, "Author 1", "Info 1"),
            new AuthorGetResponse(2L, "Author 2", "Info 2")
        );
        Page<AuthorGetResponse> mockPage = new PageImpl<>(authors);

        Mockito.when(authorService.getAllAuthors(page, size, sort)).thenReturn(mockPage);

        // Act & Assert
        mockMvc.perform(get("/admin/authors")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort[0]))
            .andExpect(status().isOk()) // HTTP 상태 확인
            .andExpect(view().name("admin/author/authors")) // 반환되는 뷰 이름 확인
            .andExpect(model().attributeExists("baseUrl")) // 모델에 필요한 속성 확인
            .andExpect(model().attributeExists("allParams"))
            .andExpect(model().attributeExists("extraParams"))
            .andExpect(model().attributeExists("authors"))
            .andExpect(model().attribute("baseUrl", "/admin/authors")) // 모델 값 검증
            .andExpect(model().attribute("allParams", Map.of("page", String.valueOf(page), "size", String.valueOf(size))))
            .andExpect(model().attribute("authors", mockPage)); // 모델에 반환된 데이터 확인

        // Service 호출 검증
        Mockito.verify(authorService, Mockito.times(1)).getAllAuthors(page, size, sort);
    }

    @Test
    void testGetAuthors_DefaultParams() throws Exception {
        // Arrange
        List<AuthorGetResponse> authors = List.of(
            new AuthorGetResponse(1L, "Default Author", "Default Info")
        );
        Page<AuthorGetResponse> mockPage = new PageImpl<>(authors);

        Mockito.when(authorService.getAllAuthors(0, 10, new String[]{"authorName:asc"})).thenReturn(mockPage);

        // Act & Assert
        mockMvc.perform(get("/admin/authors")) // 기본 파라미터로 호출
            .andExpect(status().isOk())
            .andExpect(view().name("admin/author/authors"))
            .andExpect(model().attributeExists("baseUrl", "allParams", "extraParams", "authors"))
            .andExpect(model().attribute("authors", Matchers.hasProperty("content", Matchers.hasSize(1))))
            .andExpect(model().attribute("authors", Matchers.hasProperty("content", Matchers.hasItem(
                Matchers.hasProperty("authorName", Matchers.equalTo("Default Author"))
            ))))
            .andExpect(model().attribute("baseUrl", "/admin/authors"));

        Mockito.verify(authorService, Mockito.times(1)).getAllAuthors(0, 10, new String[]{"authorName:asc"});
    }

    @Test
    void testCreateAuthorForm_Success() throws Exception {
        // Perform the GET request
        mockMvc.perform(get("/admin/authors/create"))
            .andExpect(status().isOk()) // HTTP 상태 코드가 200인지 확인
            .andExpect(view().name("admin/author/createAuthor")) // 반환된 뷰 이름 확인
            .andDo(print()); // 요청 및 응답 디버깅 정보 출력
    }

    @Test
    void testUpdateAuthorForm_Success() throws Exception {
        // Arrange
        long authorId = 1L;
        AuthorUpdateRequest mockAuthorUpdateRequest = new AuthorUpdateRequest(
            authorId, "Updated Author", "Updated Author Info"
        );

        Mockito.when(authorService.getAuthorById(authorId)).thenReturn(mockAuthorUpdateRequest);

        // Act & Assert
        mockMvc.perform(get("/admin/authors/{authorId}", authorId)) // GET 요청 수행
            .andExpect(status().isOk()) // HTTP 상태 코드 검증
            .andExpect(view().name("admin/author/updateAuthor")) // 뷰 이름 검증
            .andExpect(model().attributeExists("authorUpdateRequest")) // 모델 속성 검증
            .andExpect(model().attribute("authorUpdateRequest", mockAuthorUpdateRequest)) // 모델 값 검증
            .andDo(print()); // 요청 및 응답 디버깅 정보 출력

        // Verify that the service method was called
        Mockito.verify(authorService, Mockito.times(1)).getAuthorById(authorId);
    }

    @Test
    void testCreateAuthor_Success() throws Exception {
        // Arrange
        AuthorCreateRequest validRequest = new AuthorCreateRequest("Author Name", "Author Info");

        // Act & Assert
        mockMvc.perform(post("/admin/authors/create")
                .flashAttr("authorCreateRequest", validRequest)) // @ModelAttribute로 데이터 전달
            .andExpect(status().is3xxRedirection()) // 리다이렉션 상태 확인
            .andExpect(redirectedUrl("/admin/authors")) // 리다이렉션 주소 확인
            .andDo(print()); // 요청 및 응답 디버깅 출력

        // Verify
        Mockito.verify(authorService, Mockito.times(1)).createAuthor(Mockito.any(AuthorCreateRequest.class));
    }

    @Test
    void testDeleteAuthor_Success() throws Exception {
        // Arrange
        Long authorId = 1L;

        // Act & Assert
        mockMvc.perform(post("/admin/authors/{authorId}", authorId)
                .with(csrf())) // CSRF 보호가 활성화되어 있다면 추가 필요
            .andExpect(status().is3xxRedirection()) // 리다이렉션 상태 확인
            .andExpect(redirectedUrl("/admin/authors")) // 리다이렉션 경로 확인
            .andDo(print());

        // Verify
        Mockito.verify(authorService, Mockito.times(1)).deleteAuthor(authorId); // 서비스 호출 여부 확인
    }

}