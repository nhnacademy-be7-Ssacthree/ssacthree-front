package com.nhnacademy.mini_dooray.ssacthree_front.member.exception;

public class LoginFailedException extends RuntimeException{

    public LoginFailedException(String message) {
        super(message);
    }
}
