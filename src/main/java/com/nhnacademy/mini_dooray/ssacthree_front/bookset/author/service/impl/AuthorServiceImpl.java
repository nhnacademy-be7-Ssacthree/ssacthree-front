package com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.adapter.AuthorAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.exception.AuthorFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service.AuthorService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {
    private static final String AUTHOR_SEARCH_ERROR = "작가 정보 조회에 실패했습니다.";
    private static final String AUTHOR_CREATE_ERROR = "작가 정보 생성에 실패했습니다.";
    private static final String AUTHOR_UPDATE_ERROR = "작가 정보 수정에 실패했습니다.";
    private static final String AUTHOR_DELETE_ERROR = "작가 정보 삭제에 실패했습니다.";
    private final AuthorAdapter authorAdapter;

    @Override
    public AuthorUpdateRequest getAuthorById(Long authorId) {
        ResponseEntity<AuthorUpdateRequest> request = authorAdapter.getAuthorById(authorId);

        try {
            if (request.getStatusCode().is2xxSuccessful()) {
                return request.getBody();
            }
            throw new AuthorFailedException(AUTHOR_SEARCH_ERROR);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AuthorFailedException(AUTHOR_SEARCH_ERROR);
        }
    }

    @Override
    public Page<AuthorGetResponse> getAllAuthors(int page, int size, String[] sort) {
        ResponseEntity<Page<AuthorGetResponse>> response = authorAdapter.getAllAuthors(page, size, sort);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new AuthorFailedException(AUTHOR_SEARCH_ERROR);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AuthorFailedException(AUTHOR_SEARCH_ERROR);
        }
    }

    @Override
    public List<AuthorGetResponse> getAllAuthorList() {
        ResponseEntity<List<AuthorGetResponse>> response = authorAdapter.getAllAuthorList();

        try{
            if(response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            }
            throw new AuthorFailedException(AUTHOR_SEARCH_ERROR);
        }catch(HttpClientErrorException | HttpServerErrorException e){
            throw new AuthorFailedException(AUTHOR_SEARCH_ERROR);
        }
    }

    @Override
    public MessageResponse createAuthor(AuthorCreateRequest authorCreateRequest) {
        ResponseEntity<MessageResponse> response = authorAdapter.createAuthor(authorCreateRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new AuthorFailedException(AUTHOR_CREATE_ERROR);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AuthorFailedException(AUTHOR_CREATE_ERROR);
        }
    }

    @Override
    public MessageResponse updateAuthor(AuthorUpdateRequest authorUpdateRequest) {
        ResponseEntity<MessageResponse> response = authorAdapter.updateAuthor(authorUpdateRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new AuthorFailedException(AUTHOR_UPDATE_ERROR);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AuthorFailedException(AUTHOR_UPDATE_ERROR);
        }
    }

    @Override
    public MessageResponse deleteAuthor(Long authorId) {
        ResponseEntity<MessageResponse> response = authorAdapter.deleteAuthor(authorId);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new AuthorFailedException(AUTHOR_DELETE_ERROR);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AuthorFailedException(AUTHOR_DELETE_ERROR);
        }
    }
}
