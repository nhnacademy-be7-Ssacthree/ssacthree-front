package com.nhnacademy.mini_dooray.ssacthree_front.elastic.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.Paging;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.SearchResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.exception.InvalidPageNumberException;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.service.SearchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
  private final CategoryCommonService categoryCommonService;

  @GetMapping()
  public String searchPage(){
    return "redirect:";
  }

  /**
   * 검색 요청 처리 메서드
   *
   * @param keyword  검색 키워드
   * @param page     현재 페이지
   * @param sort     정렬 기준
   * @param pageSize 요청 데이터 수
   * @param category 카테고리 필터 -> 기본 검색 시에는 기본값 null로 검색되게 하고 카테고리 필터를 설정했을 때 값이 들어와 검색이 될 수 있게
   * @param tag      태그 필터 -> 위와 동일 단, 둘 중 하나만 사용되게
   * @param model    Thymeleaf 모델
   * @return 검색 결과 화면
   */
  @GetMapping("/books")
  public String searchBooks(
                            @RequestParam(required = false, defaultValue = "") String keyword,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "score") String sort,
                            @RequestParam(defaultValue = "20") int pageSize, //요청 데이터 수
                            @RequestParam(required = false) String category,
                            @RequestParam(required = false) String tag,
                            Model model) {

    /*
      프론트에서 page 를 1부터 시작하게 바꾼다면
      page 검사도 0 부터 예외 발생 시키기 (백엔드에서 -1 되어야 하기 때문)
      기본값 1로 변경하기
      1로 받은 걸 0으로 변환 시키기 (백엔드에서 변환? 프론트에서 변환후 전달?)

     */


    // 백엔드에서 사용할 수 있게 -1
    int requestPageNum = page - 1;
    //
    if(requestPageNum < 0 || pageSize <= 0){
      throw new InvalidPageNumberException("올바르지 않은 페이지 접근"); // 예외문 만들기?
    }



    log.info("검색 요청 - 키워드: {}, 페이지: {}, 정렬: {}, 페이지 크기: {}, 카테고리: {}, 태그: {}",
        keyword, page, sort, pageSize, category, tag);

    // 키워드 유효성 검증
    if (keyword == null || keyword.trim().isEmpty()) {
      model.addAttribute("message", "검색어를 입력해 주세요.");
      model.addAttribute("books", List.of()); // 빈 리스트 전달
      return "searchBooks"; // 검색 페이지 유지
    }

    // 카테고리 정보를 조회
    ResponseEntity<List<CategoryInfoResponse>> response = categoryCommonService.getAllCategories();
    List<CategoryInfoResponse> categories = response.getBody();

    // 키워드 유지 (정렬, 페이지 표시 개수 변경 시 사용)
    model.addAttribute("keyword", keyword);
    model.addAttribute("categories", categories);
    model.addAttribute("category", category);

    // 정렬 값이 null 또는 비어있는 경우 기본값 "score"로 설정
    if (sort == null || sort.isEmpty()) {
      sort = "score"; // 기본 정렬 값 설정
    }

    // 필터 처리: 카테고리와 태그 중 하나만 추가
    Map<String, String> filters = new HashMap<>();
    if (category != null && !category.isEmpty()) {
      filters.put("category", category);
    }
    if (tag != null && !tag.isEmpty()) {
      filters.put("tag", tag);
    }


    // 검색 서비스 호출
    SearchResponse searchResponse = searchService.searchBooks(keyword, requestPageNum, sort, pageSize, filters);
    log.info("검색완료, 1회 검색 결과: {}건", searchResponse.getBooks().size());
    int maxPage = calculateTotalPages(searchResponse.getTotalHits(), pageSize);

    // 최대 페이지보다 클 때
    if(requestPageNum >= maxPage){
      throw new InvalidPageNumberException("올바르지 않은 페이지 접근");
    }


    // 검색 결과가 없는 경우 처리
    if (searchResponse.getBooks().isEmpty()) {
      model.addAttribute("message", "\"" + keyword + "\"에 대한 검색 결과가 없습니다.");
      model.addAttribute("books", List.of());
      return "searchBooks";
    }


    // 검색 결과와 페이징 정보 구성
    model.addAttribute("books", searchResponse.getBooks());
    // Paging 객체 생성
    Paging paging = new Paging(
        page, // Thymeleaf에서 0부터 시작하도록 조정
        pageSize,
        maxPage,
        sort
    );
    log.info("Paging객체의 현재 페이지입니다. {} ", paging.getNumber());

    model.addAttribute("paging", paging);
    // 페이징 및 추가 URL 파라미터 구성
    model.addAttribute("baseUrl", "/search/books");
    model.addAttribute("extraParams", filters);

    return "searchBooks"; // 검색 결과 페이지 반환
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


  @GetMapping("/redirect")
  public String redirectCategory(@RequestParam String category) {
    ResponseEntity<List<CategoryInfoResponse>> categoryResponse = categoryCommonService.searchCategoriesByName(category);
    List<CategoryInfoResponse> categories = categoryResponse.getBody();

    if (categories != null && !categories.isEmpty()) {
      Long categoryId = categories.get(0).getCategoryId();
      return "redirect:/books?category-id=" + categoryId;
    }
    return "redirect:/searchBooks";
  }


}
