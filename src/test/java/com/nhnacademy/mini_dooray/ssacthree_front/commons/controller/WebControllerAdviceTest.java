package com.nhnacademy.mini_dooray.ssacthree_front.commons.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nhnacademy.mini_dooray.ssacthree_front.elastic.exception.InvalidPageNumberException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LoginFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.PaycoConnectionFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.SleepMemberLoginFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;


class WebControllerAdviceTest {

    private WebControllerAdvice advice;

    @BeforeEach
    public void setUp() {
        advice = new WebControllerAdvice();
    }

    @Test
    void testHandleException() {
        Exception exception = new Exception("Generic exception");
        Model model = new ConcurrentModel();
        String viewName = advice.handleException(exception, model);
        assertEquals("error", viewName);
        assertEquals("Generic exception", model.getAttribute("exception"));
    }

    @Test
    void testHandleLoginFailedException() {
        LoginFailedException exception = new LoginFailedException("Login failed");
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        String result = advice.handleLoginFailedException(exception, redirectAttributes);
        assertEquals("redirect:/", result);
        assertEquals("Login failed",
            redirectAttributes.getFlashAttributes().get("loginFailedMessage"));
    }

    @Test
    void testHandleInvalidPageException() {
        InvalidPageNumberException exception = new InvalidPageNumberException(
            "Invalid page number");
        ModelAndView modelAndView = advice.handleInvalidPageException(exception);
        assertEquals("error", modelAndView.getViewName());
        assertEquals(HttpStatus.BAD_REQUEST, modelAndView.getStatus());
        assertEquals("Invalid page number", modelAndView.getModel().get("exception"));
    }

    @Test
    void testHandlePaycoConnectionFailedException() {
        PaycoConnectionFailedException exception = new PaycoConnectionFailedException(
            "Connection failed");
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        String result = advice.handlePaycoConnectionFailedException(exception, redirectAttributes);
        assertEquals("redirect:/members/my-page", result);
        assertEquals("Connection failed",
            redirectAttributes.getFlashAttributes().get("connectionResult"));
    }


    @Test
    void testHandleSleepMemberLoginFailedException() {
        SleepMemberLoginFailedException exception = new SleepMemberLoginFailedException(
            "Sleep member login failed", "testUser");
        Model model = new ConcurrentModel();
        String viewName = advice.handleSleepMemberLoginFailedException(exception, model);
        assertEquals("sleepMember", viewName);
        assertEquals("testUser", model.getAttribute("memberLoginId"));
    }
}
