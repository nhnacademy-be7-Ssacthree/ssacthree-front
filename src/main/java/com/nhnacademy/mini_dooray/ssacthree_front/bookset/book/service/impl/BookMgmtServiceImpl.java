package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter.BookMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.exception.BookFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookMgmtServiceImpl implements BookMgmtService {
    private static final String BOOK_SEARCH_ERROR = "책 정보 조회에 실패했습니다.";
    private static final String BOOK_CREATE_ERROR = "책 정보 생성에 실패했습니다.";

    private final BookMgmtAdapter bookMgmtAdapter;

    @Override
    public List<BookInfoResponse> getAllBooks(){
         ResponseEntity<List<BookInfoResponse>> response = bookMgmtAdapter.getAllBooks();

        try{
            if(response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new BookFailedException(BOOK_SEARCH_ERROR);
        }catch(HttpClientErrorException | HttpServerErrorException e) {
            throw new BookFailedException(BOOK_SEARCH_ERROR);
        }
    }

    @Override
    public MessageResponse createBook(BookSaveRequest bookSaveRequest){
        ResponseEntity<MessageResponse> response = bookMgmtAdapter.createBook(bookSaveRequest);

        try{
            if(response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            }
            throw new IllegalStateException(BOOK_CREATE_ERROR);
        }catch(HttpClientErrorException | HttpServerErrorException e){
            throw new BookFailedException(BOOK_CREATE_ERROR);
        }
    }

    @Override
    public MessageResponse updateBook(Long bookId, BookSaveRequest bookSaveRequest){
        return null;
    }

}
