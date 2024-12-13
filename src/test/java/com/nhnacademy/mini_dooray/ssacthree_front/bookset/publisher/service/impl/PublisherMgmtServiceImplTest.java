package com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.adapter.PublisherMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.exception.PublisherCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.exception.PublisherGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.exception.PublisherUpdateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublisherMgmtServiceImplTest {

    @Mock
    private PublisherMgmtAdapter publisherMgmtAdapter;

    @InjectMocks
    private PublisherMgmtServiceImpl publisherMgmtService;

    private PublisherCreateRequest publisherCreateRequest;
    private PublisherUpdateRequest publisherUpdateRequest;
    private PublisherGetResponse publisherGetResponse;

    @BeforeEach
    public void setUp() {
        publisherCreateRequest = new PublisherCreateRequest();
        publisherCreateRequest.setPublisherName("Test Publisher");

        publisherUpdateRequest = new PublisherUpdateRequest(1L);

        publisherGetResponse = new PublisherGetResponse(1L, "Test Publisher", true);
    }

    @Test
    public void testGetAllPublishers_Success() {
        List<PublisherGetResponse> publisherList = Arrays.asList(publisherGetResponse);
        Page<PublisherGetResponse> page = new PageImpl<>(publisherList);

        when(publisherMgmtAdapter.getAllPublishers(0, 10, new String[]{}))
                .thenReturn(new ResponseEntity<>(page, HttpStatus.OK));

        Page<PublisherGetResponse> result = publisherMgmtService.getAllPublishers(0, 10, new String[]{});

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Publisher", result.getContent().get(0).getPublisherName());
    }

    @Test
    public void testGetAllPublisherList_Success() {
        List<PublisherGetResponse> publisherList = Arrays.asList(publisherGetResponse);

        when(publisherMgmtAdapter.getAllPublisherList())
                .thenReturn(new ResponseEntity<>(publisherList, HttpStatus.OK));

        List<PublisherGetResponse> result = publisherMgmtService.getAllPublisherList();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Publisher", result.get(0).getPublisherName());
    }

    @Test
    public void testCreatePublisher_Success() {
        MessageResponse messageResponse = new MessageResponse("출판사 생성에 성공하였습니다.");

        when(publisherMgmtAdapter.createPublisher(publisherCreateRequest))
                .thenReturn(new ResponseEntity<>(messageResponse, HttpStatus.CREATED));

        MessageResponse result = publisherMgmtService.createPublisher(publisherCreateRequest);

        assertNotNull(result);
        assertEquals("출판사 생성에 성공하였습니다.", result.getMessage());
    }

    @Test
    public void testCreatePublisher_Fail() {
        when(publisherMgmtAdapter.createPublisher(publisherCreateRequest))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(PublisherCreateFailedException.class, () -> {
            publisherMgmtService.createPublisher(publisherCreateRequest);
        });
    }

    @Test
    public void testUpdatePublisher_Success() {
        MessageResponse messageResponse = new MessageResponse("출판사 수정에 성공하였습니다.");

        when(publisherMgmtAdapter.updatePublisher(publisherUpdateRequest))
                .thenReturn(new ResponseEntity<>(messageResponse, HttpStatus.OK));

        MessageResponse result = publisherMgmtService.updatePublisher(publisherUpdateRequest);

        assertNotNull(result);
        assertEquals("출판사 수정에 성공하였습니다.", result.getMessage());
    }

    @Test
    public void testUpdatePublisher_Fail() {
        when(publisherMgmtAdapter.updatePublisher(publisherUpdateRequest))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(PublisherUpdateFailedException.class, () -> {
            publisherMgmtService.updatePublisher(publisherUpdateRequest);
        });
    }

    @Test
    public void testGetAllPublishers_Fail() {
        when(publisherMgmtAdapter.getAllPublishers(0, 10, new String[]{}))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(PublisherGetFailedException.class, () -> {
            publisherMgmtService.getAllPublishers(0, 10, new String[]{});
        });
    }

    @Test
    public void testGetAllPublisherList_Fail() {
        when(publisherMgmtAdapter.getAllPublisherList())
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(PublisherGetFailedException.class, () -> {
            publisherMgmtService.getAllPublisherList();
        });
    }
}
