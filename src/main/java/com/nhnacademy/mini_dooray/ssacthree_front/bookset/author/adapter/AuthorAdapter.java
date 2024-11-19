package com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "authorSendClient", url = "${admin-client.url}", contextId = "authorClient")
public interface AuthorAdapter {

    @GetMapping("/authors")
    ResponseEntity<Page<AuthorGetResponse>> getAllAuthors(@RequestParam("page") int page,
                                                          @RequestParam("size") int size,
                                                          @RequestParam("sort") String[] sort);

    @PostMapping("/authors")
    ResponseEntity<MessageResponse> createAuthor(@RequestBody AuthorCreateRequest authorCreateRequest);

    @PutMapping("/authors")
    ResponseEntity<MessageResponse> updateAuthor(@RequestBody AuthorUpdateRequest authorUpdateRequest);

    @DeleteMapping("/authors/{authorId}")
    ResponseEntity<MessageResponse> deleteAuthor(@PathVariable Long authorId);

    @GetMapping("/authors/{authorId}")
    ResponseEntity<AuthorUpdateRequest> getAuthorById(@PathVariable Long authorId);

}
