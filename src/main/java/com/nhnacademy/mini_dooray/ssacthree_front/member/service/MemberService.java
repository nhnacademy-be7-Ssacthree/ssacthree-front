package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    MessageResponse memberRegister(MemberRegisterRequest request);
    MessageResponse memberLogin(MemberLoginRequest requestBody, HttpServletResponse httpServletResponse);
    MessageResponse memberLogout();
}
