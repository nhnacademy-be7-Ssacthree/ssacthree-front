package com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.adapter.MemberGradeAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto.MemberGradeCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto.MemberGradeGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.exception.MemberGradeCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.exception.MemberGradeDeleteFailException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.exception.MemberGradeGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import feign.FeignException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MemberGradeServiceImplTest {

    private MemberGradeAdapter memberGradeAdapter;
    private MemberGradeServiceImpl memberGradeService;

    @BeforeEach
    public void setUp() {
        memberGradeAdapter = mock(MemberGradeAdapter.class);
        memberGradeService = new MemberGradeServiceImpl(memberGradeAdapter);
    }

    @Test
    public void testDeleteMemberGrade_Success() {
        // Arrange
        Long memberGradeId = 1L;
        MessageResponse responseMessage = new MessageResponse("Success");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(responseMessage,
            HttpStatus.OK);

        when(memberGradeAdapter.deleteMemberGrade(memberGradeId)).thenReturn(responseEntity);

        // Act
        MessageResponse result = memberGradeService.deleteMemberGrade(memberGradeId);

        // Assert
        assertEquals(responseMessage, result);
        verify(memberGradeAdapter, times(1)).deleteMemberGrade(memberGradeId);
    }

    @Test
    public void testDeleteMemberGrade_Non2xxResponse() {
        // Arrange
        Long memberGradeId = 1L;
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(null,
            HttpStatus.BAD_REQUEST);

        when(memberGradeAdapter.deleteMemberGrade(memberGradeId)).thenReturn(responseEntity);

        // Act & Assert
        assertThrows(MemberGradeDeleteFailException.class,
            () -> memberGradeService.deleteMemberGrade(memberGradeId));
        verify(memberGradeAdapter, times(1)).deleteMemberGrade(memberGradeId);
    }

    @Test
    public void testDeleteMemberGrade_FeignException() {
        // Arrange
        Long memberGradeId = 1L;

        when(memberGradeAdapter.deleteMemberGrade(memberGradeId)).thenThrow(FeignException.class);

        // Act & Assert
        assertThrows(MemberGradeDeleteFailException.class,
            () -> memberGradeService.deleteMemberGrade(memberGradeId));
        verify(memberGradeAdapter, times(1)).deleteMemberGrade(memberGradeId);
    }

    @Test
    public void testMemberGradeCreate_Success() {
        // Arrange
        MemberGradeCreateRequest request = new MemberGradeCreateRequest("Gold", 0.15f);
        MessageResponse responseMessage = new MessageResponse("Success");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(responseMessage,
            HttpStatus.CREATED);

        when(memberGradeAdapter.createMemberGrade(request)).thenReturn(responseEntity);

        // Act
        MessageResponse result = memberGradeService.memberGradeCreate(request);

        // Assert
        assertEquals(responseMessage, result);
        verify(memberGradeAdapter, times(1)).createMemberGrade(request);
    }

    @Test
    public void testMemberGradeCreate_Non2xxResponse() {
        // Arrange
        MemberGradeCreateRequest request = new MemberGradeCreateRequest("Gold", 0.15f);
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(null,
            HttpStatus.BAD_REQUEST);

        when(memberGradeAdapter.createMemberGrade(request)).thenReturn(responseEntity);

        // Act & Assert
        assertThrows(MemberGradeCreateFailedException.class,
            () -> memberGradeService.memberGradeCreate(request));
        verify(memberGradeAdapter, times(1)).createMemberGrade(request);
    }

    @Test
    public void testMemberGradeCreate_FeignException() {
        // Arrange
        MemberGradeCreateRequest request = new MemberGradeCreateRequest("Gold", 0.15f);

        when(memberGradeAdapter.createMemberGrade(request)).thenThrow(FeignException.class);

        // Act & Assert
        assertThrows(MemberGradeCreateFailedException.class,
            () -> memberGradeService.memberGradeCreate(request));
        verify(memberGradeAdapter, times(1)).createMemberGrade(request);
    }

    @Test
    public void testGetAllMemberGrade_Success() {
        // Arrange
        List<MemberGradeGetResponse> responseList = Collections.singletonList(
            new MemberGradeGetResponse(/* Initialize with appropriate values */)
        );
        ResponseEntity<List<MemberGradeGetResponse>> responseEntity = new ResponseEntity<>(
            responseList, HttpStatus.OK);

        when(memberGradeAdapter.getAllMemberGrades()).thenReturn(responseEntity);

        // Act
        List<MemberGradeGetResponse> result = memberGradeService.getAllMemberGrade();

        // Assert
        assertEquals(responseList, result);
        verify(memberGradeAdapter, times(1)).getAllMemberGrades();
    }

    @Test
    public void testGetAllMemberGrade_Non2xxResponse() {
        // Arrange
        ResponseEntity<List<MemberGradeGetResponse>> responseEntity = new ResponseEntity<>(null,
            HttpStatus.BAD_REQUEST);

        when(memberGradeAdapter.getAllMemberGrades()).thenReturn(responseEntity);

        // Act & Assert
        assertThrows(MemberGradeGetFailedException.class,
            () -> memberGradeService.getAllMemberGrade());
        verify(memberGradeAdapter, times(1)).getAllMemberGrades();
    }

    @Test
    public void testGetAllMemberGrade_FeignException() {
        // Arrange
        when(memberGradeAdapter.getAllMemberGrades()).thenThrow(FeignException.class);

        // Act & Assert
        assertThrows(MemberGradeGetFailedException.class,
            () -> memberGradeService.getAllMemberGrade());
        verify(memberGradeAdapter, times(1)).getAllMemberGrades();
    }
}
