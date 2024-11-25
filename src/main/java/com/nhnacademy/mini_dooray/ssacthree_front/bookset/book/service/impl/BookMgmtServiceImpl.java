package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter.BookMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookDeleteRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponseMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.exception.BookFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.image.adapter.ImageUploadAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class BookMgmtServiceImpl implements BookMgmtService {
    private static final String BOOK_SEARCH_ERROR = "책 정보 조회에 실패했습니다.";
    private static final String BOOK_CREATE_ERROR = "책 정보 생성에 실패했습니다.";
    private static final String BOOK_UPDATE_ERROR = "책 정보 수정에 실패했습니다.";
    private static final String BOOK_DELETE_ERROR = "책 정보 삭제에 실패했습니다.";
    private static final String IMAGE_PATH = "/ssacthree/bookImage/";

    private final BookMgmtAdapter bookMgmtAdapter;
    private final ImageUploadAdapter imageUploadAdapter;

    @Override
    public Page<BookSearchResponse> getAllBooks(int page, int size, String[] sort){
        ResponseEntity<Page<BookSearchResponse>> response = bookMgmtAdapter.getAllBooks(page, size, sort);

        try{
            if(response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new BookFailedException(BOOK_SEARCH_ERROR);
        }catch(HttpClientErrorException | HttpServerErrorException e) {
            throw new BookFailedException(BOOK_SEARCH_ERROR, e);
        }
    }

    @Override
    public MessageResponse createBook(BookSaveRequestMultipart bookSaveRequestMultipart, MultipartFile bookThumbnailUrlMultipartFile){
        String imageUrl = getImageUrl(bookThumbnailUrlMultipartFile);
        BookSaveRequest bookSaveRequest = convertToBookSaveRequest(bookSaveRequestMultipart, imageUrl);

        ResponseEntity<MessageResponse> response = bookMgmtAdapter.createBook(bookSaveRequest);

        try{
            if(response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            }
            throw new BookFailedException(BOOK_CREATE_ERROR);
        }catch(HttpClientErrorException | HttpServerErrorException e){
            throw new BookFailedException(BOOK_CREATE_ERROR + e.getMessage());
        }
    }

    @Override
    public MessageResponse updateBook(BookUpdateRequestMultipart bookUpdateRequestMultipart, MultipartFile bookThumbnailUrlMultipartFile){
        String imageUrl = getImageUrl(bookThumbnailUrlMultipartFile);
        BookUpdateRequest bookUpdateRequest = convertToBookUpdateRequest(bookUpdateRequestMultipart, imageUrl);

        ResponseEntity<MessageResponse> response = bookMgmtAdapter.updateBook(bookUpdateRequest);


        try{
            if(response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            }
            throw new IllegalStateException(BOOK_UPDATE_ERROR);
        }catch(HttpClientErrorException | HttpServerErrorException e){
            throw new BookFailedException(BOOK_UPDATE_ERROR);
        }
    }

    @Override
    public MessageResponse deleteBook(Long bookId){
        ResponseEntity<MessageResponse> response = bookMgmtAdapter.deleteBook(bookId);

        try{
            if(response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            }
            throw new IllegalStateException(BOOK_DELETE_ERROR);
        }catch(HttpClientErrorException | HttpServerErrorException e){
            throw new BookFailedException(BOOK_DELETE_ERROR);
        }
    }

    @Override
    public BookInfoResponse getBookById(Long bookId){
        ResponseEntity<BookInfoResponse> response = bookMgmtAdapter.getBookByBookId(bookId);

        try{
            if(response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            }
            throw new BookFailedException(BOOK_SEARCH_ERROR);
        }catch(HttpClientErrorException | HttpServerErrorException e){
            throw new BookFailedException(BOOK_SEARCH_ERROR);
        }
    }

    @Override
    public String getImageUrl(MultipartFile bookThumbnailUrl){
        return imageUploadAdapter.uploadImage(bookThumbnailUrl, IMAGE_PATH);
    }


    private BookSaveRequest convertToBookSaveRequest(BookSaveRequestMultipart bookSaveRequestMultipart, String imageUrl) {
        return new BookSaveRequest(
            bookSaveRequestMultipart.getBookId(),
            bookSaveRequestMultipart.getBookName(),
            bookSaveRequestMultipart.getBookIndex(),
            bookSaveRequestMultipart.getBookInfo(),
            bookSaveRequestMultipart.getBookIsbn(),
            bookSaveRequestMultipart.getPublicationDate(),
            bookSaveRequestMultipart.getRegularPrice(),
            bookSaveRequestMultipart.getSalePrice(),
            bookSaveRequestMultipart.getIsPacked(),
            bookSaveRequestMultipart.getStock(),
            imageUrl, // 이미지 URL을 설정
            bookSaveRequestMultipart.getBookViewCount(),
            bookSaveRequestMultipart.getBookDiscount(),
            bookSaveRequestMultipart.getBookStatus(),
            bookSaveRequestMultipart.getPublisherId(),
            bookSaveRequestMultipart.getCategoryIdList(),
            bookSaveRequestMultipart.getAuthorIdList(),
            bookSaveRequestMultipart.getTagIdList()
        );

    }

    public BookUpdateRequest convertToBookUpdateRequest(BookUpdateRequestMultipart bookUpdateRequestMultipart, String imageUrl) {
        BookUpdateRequest bookUpdateRequest = new BookUpdateRequest();

        bookUpdateRequest.setBookId(bookUpdateRequestMultipart.getBookId());
        bookUpdateRequest.setBookName(bookUpdateRequestMultipart.getBookName());
        bookUpdateRequest.setBookIndex(bookUpdateRequestMultipart.getBookIndex());
        bookUpdateRequest.setBookInfo(bookUpdateRequestMultipart.getBookInfo());
        bookUpdateRequest.setBookIsbn(bookUpdateRequestMultipart.getBookIsbn());
        bookUpdateRequest.setPublicationDate(bookUpdateRequestMultipart.getPublicationDate());
        bookUpdateRequest.setRegularPrice(bookUpdateRequestMultipart.getRegularPrice());
        bookUpdateRequest.setSalePrice(bookUpdateRequestMultipart.getSalePrice());
        bookUpdateRequest.setIsPacked(bookUpdateRequestMultipart.getIsPacked());
        bookUpdateRequest.setStock(bookUpdateRequestMultipart.getStock());

        bookUpdateRequest.setBookThumbnailImageUrl(imageUrl != null ? imageUrl : bookUpdateRequestMultipart.getBookThumbnailImageUrl());

        bookUpdateRequest.setBookDiscount(bookUpdateRequestMultipart.getBookDiscount());
        bookUpdateRequest.setBookStatus(bookUpdateRequestMultipart.getBookStatus());
        bookUpdateRequest.setPublisherId(bookUpdateRequestMultipart.getPublisherId());
        bookUpdateRequest.setCategoryIdList(bookUpdateRequestMultipart.getCategoryIdList());
        bookUpdateRequest.setTagIdList(bookUpdateRequestMultipart.getTagIdList());
        bookUpdateRequest.setAuthorIdList(bookUpdateRequestMultipart.getAuthorIdList());

        return bookUpdateRequest;
    }

}
