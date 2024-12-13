package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.member.service.CertNumberService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertNumberServiceImpl implements CertNumberService {

    private final StringRedisTemplate stringRedisTemplate;
    private static final String CERT_PREFIX = "cert:";

    @Override
    public void saveCertNumber(String memberLoginId, String certNumber) {
        stringRedisTemplate.opsForValue()
            .set(CERT_PREFIX + memberLoginId, certNumber, 5, TimeUnit.MINUTES);
    }


    @Override
    public String getCertNumber(String memberLoginId) {
        return stringRedisTemplate.opsForValue().get(CERT_PREFIX + memberLoginId);
    }

    @Override
    public boolean delete(String memberLoginId) {
        return Boolean.TRUE.equals(stringRedisTemplate.delete(CERT_PREFIX + memberLoginId));
    }
}
