package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;

public class Dooray {

    String botName;
    String text;

    public Dooray(String text) {
        this.botName = "인증 봇";
        this.text = text;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
