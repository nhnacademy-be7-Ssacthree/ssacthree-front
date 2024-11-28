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

    @Override
    public void saveCertNumber(String memberLoginId, String certNumber) {
        stringRedisTemplate.opsForValue()
            .set("cert:" + memberLoginId, certNumber, 5, TimeUnit.MINUTES);
    }

    @Override
    public String getCertNumber(String memberLoginId) {
        return stringRedisTemplate.opsForValue().get("cert:" + memberLoginId);
    }

    @Override
    public boolean delete(String memberLoginId) {
        return Boolean.TRUE.equals(stringRedisTemplate.delete("cert:" + memberLoginId));
    }
}
