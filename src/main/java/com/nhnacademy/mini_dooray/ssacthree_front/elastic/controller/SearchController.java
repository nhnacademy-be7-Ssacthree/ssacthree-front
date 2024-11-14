package com.nhnacademy.mini_dooray.ssacthree_front.elastic.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.elastic.domain.BookDocument;
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

  // 화면 로드를 위한 GET 메서드
  @GetMapping("/bookPage")
  public String showSearchPage() {
    return "books";  // books.html을 반환하여 화면을 로드
  }


  @GetMapping("/books")
  public String searchBooks(@RequestParam String keyword,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(required = false) String sort,
                            @RequestParam(required = false, defaultValue = "20") int pageSize,
                            @RequestParam(required = false) String category,
                            @RequestParam(required = false) String tag,
                            Model model) {
    log.info("검색을 시도합니다. {}", keyword);
    Map<String, String> filters = new HashMap<>();
    if (category != null) {
      filters.put("category", category);
    }
    if (tag != null) {
      filters.put("tag", tag);
    }
    // SearchService를 통해 백엔드로 데이터 전달 후 결과 받기
    List<BookDocument> books = searchService.searchBooks(keyword, page, sort, pageSize, filters);
    log.info("books 리스트를 전달 받았습니다. {}" ,books);
    model.addAttribute("books", books);  // 결과를 Model에 담아 view로 전달
    return "books";  // books.html로 결과 전달
  }
}
