package com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.apache.logging.log4j.message.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="authorSendClient", url = "${admin-client.url}")
public interface AuthorAdapter {

    @GetMapping("/authors")
    ResponseEntity<List<AuthorGetResponse>> getAllAuthors();

    @PostMapping("/authors/create")
    ResponseEntity<MessageResponse> createAuthor(@RequestBody AuthorCreateRequest authorCreateRequest);

    @PostMapping("/authors/update")
    ResponseEntity<MessageResponse> updateAuthor(@RequestBody AuthorUpdateRequest authorUpdateRequest);

    @DeleteMapping("/authors/delete}")
    ResponseEntity<MessageResponse> deleteAuthor();

    @GetMapping("/authors/{authorId}")
    ResponseEntity<AuthorUpdateRequest> getAuthorById(@PathVariable long authorId);

}
