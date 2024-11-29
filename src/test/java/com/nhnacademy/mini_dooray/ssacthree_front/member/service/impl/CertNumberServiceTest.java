package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class CertNumberServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private CertNumberServiceImpl certNumberService;

    @Test
    void testSaveCertNumber() {
        // Arrange
        String memberLoginId = "testUser";
        String certNumber = "123456";
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        certNumberService.saveCertNumber(memberLoginId, certNumber);

        // Assert
        verify(valueOperations, times(1))
            .set("cert:" + memberLoginId, certNumber, 5, TimeUnit.MINUTES);
    }

    @Test
    void testGetCertNumber() {
        // Arrange
        String memberLoginId = "testUser";
        String expectedCertNumber = "123456";
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("cert:" + memberLoginId)).thenReturn(expectedCertNumber);

        // Act
        String actualCertNumber = certNumberService.getCertNumber(memberLoginId);

        // Assert
        assertEquals(expectedCertNumber, actualCertNumber);
        verify(valueOperations, times(1)).get("cert:" + memberLoginId);
    }

    @Test
    void testDeleteCertNumber() {
        // Arrange
        String memberLoginId = "testUser";
        when(stringRedisTemplate.delete("cert:" + memberLoginId)).thenReturn(true);

        // Act
        boolean isDeleted = certNumberService.delete(memberLoginId);

        // Assert
        assertTrue(isDeleted);
        verify(stringRedisTemplate, times(1)).delete("cert:" + memberLoginId);
    }
}