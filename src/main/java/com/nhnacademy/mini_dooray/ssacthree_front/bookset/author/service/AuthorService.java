package com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorService {
    AuthorUpdateRequest getAuthorById(Long authorId);

    Page<AuthorGetResponse> getAllAuthors(int page, int size, String[] sort);

    List<AuthorGetResponse> getAllAuthorList();

    MessageResponse createAuthor(AuthorCreateRequest authorCreateRequest);

    MessageResponse updateAuthor(AuthorUpdateRequest authorUpdateRequest);

    MessageResponse deleteAuthor(Long authorId);
}
