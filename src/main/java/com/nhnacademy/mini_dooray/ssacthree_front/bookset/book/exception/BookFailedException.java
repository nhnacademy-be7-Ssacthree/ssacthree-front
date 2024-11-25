package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.exception;

public class BookFailedException extends RuntimeException {
    public BookFailedException(String message) {
        super(message);
    }
    public BookFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
