package com.nhnacademy.mini_dooray.ssacthree_front.elastic.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.elastic.domain.BookDocument;
import java.util.List;
import java.util.Map;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public class SearchContoller {
  /**
   * 검색 결과 페이지 표시
   *
   * @param query  검색 키워드
   * @param page   페이지 번호 (기본값: 1)
   * @param sort   정렬 옵션 (예: 조회수, 최신순 등)
   * @param filters 필터 (출판사, 저자 등 추가 필터링 조건)
   * @param model  검색 결과를 전달할 모델
   * @return 검색 결과 페이지 (books.html)
   */
  @GetMapping("/books")
  public String getBooksPage(
      @RequestParam(value = "query", required = false, defaultValue = "") String query,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam Map<String, String> filters,
      Model model) {

    if (query.isEmpty()) {
      model.addAttribute("errorMessage", "검색어를 입력하세요.");
      return "books";
    }

    // 페이지 번호가 1보다 작을 경우 1로 설정
    page = Math.max(page, 1);

    // 검색 결과를 Elasticsearch에서 가져옴 (백엔드 shop-api 에서 가져오기)
//    List<BookDocument> books = elasticService.searchBooks(query, page, sort, filters);

    // 모델에 검색 결과 추가
//    model.addAttribute("books", books); 받아오면 주석 해제하기
    model.addAttribute("query", query);
    model.addAttribute("page", page);
    model.addAttribute("sort", sort);

    return "books";  // 검색 결과 페이지로 이동
  }

  /**
   * 이건 기존 book에서 만든 상세페이지 코드를 활용하는게 좋을 듯
   * 책 상세 페이지 표시
   *
   * @param id    도서 ID
   * @param model 상세 정보를 전달할 모델
   * @return 상세 페이지 (bookDetail.html)
   */
  @GetMapping("/books/{book-id}")
  public String getBookDetail(@PathVariable(name = "book-id") Long id, Model model) {
//    BookDocument book = elasticService.getBookById(id);
//    if (book == null) {
//      model.addAttribute("errorMessage", "해당 도서를 찾을 수 없습니다.");
//      return "error"; // 오류 페이지로 이동
//    }
//    model.addAttribute("book", book);
//    return "bookDetail"; // 책 상세 페이지로 이동
  }
}
