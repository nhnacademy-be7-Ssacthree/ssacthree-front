package com.nhnacademy.mini_dooray.ssacthree_front.member.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.Dooray;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "doorayMessenger", url = "https://hook.dooray.com/services/3204376758577275363/3946049050420084467/yZDScYAEQAu3H6TtaNF8cw")
public interface DoorayAdapter {

    @PostMapping
    void sendMessage(@RequestBody Dooray dooray);
}
