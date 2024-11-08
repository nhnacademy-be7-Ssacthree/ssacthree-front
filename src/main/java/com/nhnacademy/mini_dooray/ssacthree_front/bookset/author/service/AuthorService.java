package com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.apache.logging.log4j.message.Message;

import java.util.List;

public interface AuthorService {
    AuthorUpdateRequest getAuthorById(long authorId);
    List<AuthorGetResponse> getAllAuthors();
    MessageResponse createAuthor(AuthorCreateRequest authorCreateRequest);
    MessageResponse updateAuthor(AuthorUpdateRequest authorUpdateRequest);
    MessageResponse deleteAuthor(long authorId);
}
