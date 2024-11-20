package com.nhnacademy.mini_dooray.ssacthree_front.elastic.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.elastic.domain.BookDocument;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.Paging;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.service.SearchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/search")
public class SearchController {

  private final SearchService searchService;


  /**
   * 검색 요청 처리 메서드
   *
   * @param keyword  검색 키워드
   * @param page     현재 페이지
   * @param sort     정렬 기준
   * @param pageSize 페이지 크기
   * @param category 카테고리 필터
   * @param tag      태그 필터
   * @param model    Thymeleaf 모델
   * @return 검색 결과 화면
   */
  @GetMapping("/books")
  public String searchBooks(
      @RequestParam(required = false, defaultValue = "") String keyword,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "score") String sort,
      @RequestParam(defaultValue = "20") int pageSize,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String tag,
      Model model) {
    log.info("{}, 정렬정렬", sort);
    // 키워드 유지 (정렬, 페이지 표시 개수 변경 시 사용)
    model.addAttribute("keyword", keyword);

    if (keyword == null || keyword.trim().isEmpty()) {
      model.addAttribute("message", "검색어를 입력해 주세요.");
      model.addAttribute("books", List.of()); // 빈 리스트 전달
      return "books"; // 현재 페이지 유지
    }
    log.info("현재 페이지: {}, 페이지 크기: {}", page, pageSize);
    log.info("검색 요청 - 키워드: {}, 페이지: {}, 정렬: {}", keyword, page, sort);

    // 정렬 값이 null 또는 비어있는 경우 기본값 "score"로 설정
    if (sort == null || sort.isEmpty()) {
      sort = "score"; // 기본 정렬 값 설정
    }

    // 필터 조건 생성 -> 수정 필요
    Map<String, String> filters = new HashMap<>();
    addFilterIfNotNull(filters, "category", category);
    addFilterIfNotNull(filters, "tag", tag);

    // 검색 서비스 호출
    List<BookDocument> books = searchService.searchBooks(keyword, page, sort, pageSize, filters);
    log.info("검색 결과: {}건", books.size());

    // 검색 결과가 없는 경우
    if (books.isEmpty()) {
      model.addAttribute("message", "\"" + keyword + "\"에 대한 검색 결과가 없습니다.");
      model.addAttribute("books", List.of());
      return "books";
    }


    // 검색 결과와 페이징 정보 구성
    model.addAttribute("books", books);

    // Paging 객체 생성
    Paging paging = new Paging(
        page - 1, // Thymeleaf에서 0부터 시작하도록 조정
        pageSize,
        calculateTotalPages(books.size(), pageSize),
        sort
    );
    model.addAttribute("paging", paging);

    // 페이징 및 추가 URL 파라미터 구성
    model.addAttribute("baseUrl", "/search/books");
    Map<String, String> extraParams = new HashMap<>();
    extraParams.put("keyword", keyword);
    if (category != null) extraParams.put("category", category);
    if (tag != null) extraParams.put("tag", tag);
    model.addAttribute("extraParams", extraParams);

    return "books"; // 검색 결과 페이지 반환
  }

  /**
   * 총 페이지 수 계산
   *
   * @param totalItems 총 데이터 개수
   * @param pageSize   페이지 크기
   * @return 총 페이지 수
   */
  private int calculateTotalPages(int totalItems, int pageSize) {
    return (int) Math.ceil((double) totalItems / pageSize);
  }


/**
 * 필터 조건을 Map에 추가하는 유틸 메서드
 *
 * @param filters 필터를 저장할 Map 객체
 * @param key     필터의 키 (예: "category", "tag")
 * @param value   필터의 값 (예: "소설", "베스트셀러")
 */
  private void addFilterIfNotNull(Map<String, String> filters, String key, String value) {
    if (value != null && !value.isEmpty()) {
      filters.put(key, value);
    }
  }
}
