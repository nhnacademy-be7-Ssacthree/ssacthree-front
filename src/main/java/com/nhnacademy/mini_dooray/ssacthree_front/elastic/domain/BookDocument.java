package com.nhnacademy.mini_dooray.ssacthree_front.elastic.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "ssacthree_books")  // Elasticsearch 인덱스 설정
public class BookDocument {
  @Id
  private Long bookId;
  private String bookName;
  private String bookIndex;
  private String bookInfo;
  private String bookIsbn;
  private String publicationDate;
  private Integer regularPrice; // int -> Integer로 변경
  private Integer salePrice;    // int -> Integer로 변경
  private boolean isPacked;
  private Integer stock;        // int -> Integer로 변경
  private String bookThumbnailImageUrl;
  private Integer bookViewCount; // int -> Integer로 변경
  private Integer bookDiscount;  // int -> Integer로 변경
  private String publisherNames;
  private String authorNames;

  // 여러 태그를 저장할 수 있도록 List<String> 타입
  private List<String> tagNames;
  // 여러 카테고리를 저장할 수 있도록 List<String> 타입
  private List<String> category;
}
