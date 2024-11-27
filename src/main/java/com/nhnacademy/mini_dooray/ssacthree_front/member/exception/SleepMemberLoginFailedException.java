package com.nhnacademy.mini_dooray.ssacthree_front.member.exception;

import lombok.Getter;

public class SleepMemberLoginFailedException extends RuntimeException {

    @Getter
    private String memberLoginId;

    public SleepMemberLoginFailedException(String message, String memberLoginId) {
        super(message);
        this.memberLoginId = memberLoginId;
    }
}
