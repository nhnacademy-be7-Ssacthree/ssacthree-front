package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

public interface CertNumberService {

    void saveCertNumber(String memberLoginId, String certNumber);

    String getCertNumber(String memberLoginId);

    boolean delete(String memberLoginId);
}
