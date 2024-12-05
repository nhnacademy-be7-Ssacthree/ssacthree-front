package com.nhnacademy.mini_dooray.ssacthree_front.image;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nhnacademy.mini_dooray.ssacthree_front.image.adapter.ImageUploadAdapter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ImageUploadAdapterTest {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private MultipartFile multipartFile;

  @InjectMocks
  private ImageUploadAdapter imageUploadAdapter;
  private MockMvc mockMvc;


  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(imageUploadAdapter).build();

    MockitoAnnotations.openMocks(this);

    String responseBody = "{\"header\":{\"isSuccessful\":true,\"resultMessage\":\"Success\",\"resultCode\":0}}";
    ResponseEntity<String> fakeResponse = new ResponseEntity<>(
        "{\"header\":{\"isSuccessful\":true,\"resultMessage\":\"Success\"}}", HttpStatus.OK);

// Mock 객체가 호출될 때 fakeResponse를 반환하도록 설정
    lenient().when(restTemplate.exchange(
        any(URI.class),
        eq(HttpMethod.PUT),
        any(HttpEntity.class),
        eq(String.class))
    ).thenReturn(fakeResponse);
  }

  @Test
  void testUpdateImagePath() {
    // Given
    String basePath = "/ssacthree/packaging/";
    ByteArrayResource imageResource = new ByteArrayResource("Test Content".getBytes()) {
      @Override
      public String getFilename() {
        return "testImage.jpg";
      }
    };

    // When
    String updatedPath = ImageUploadAdapter.updateImagePath(basePath, imageResource);

    // Then
    assertEquals("/ssacthree/packaging/testImage.jpg", updatedPath);
  }




//  @Test
//  void testImageUpload() throws Exception {
//    // 1. Mock 파일 생성
//    MockMultipartFile mockFile = new MockMultipartFile(
//        "file",
//        "test-image.jpg",
//        "image/jpeg",
//        "dummy image content".getBytes());
//
//    // 2. 가짜 서버 응답 설정 (성공한 응답)
//    String fakeResponseBody = "{\"header\":{\"isSuccessful\":true,\"resultMessage\":\"Success\"}, \"file\": {\"url\": \"http://example.com/test-image.jpg\"}}";
//    ResponseEntity<String> fakeResponse = new ResponseEntity<>(fakeResponseBody, HttpStatus.OK);
//
//    // 3. RestTemplate 모킹
//    RestTemplate restTemplate = mock(RestTemplate.class);
//    when(restTemplate.exchange(
//        any(URI.class),
//        eq(HttpMethod.PUT),
//        any(HttpEntity.class),
//        eq(String.class)
//    )).thenReturn(fakeResponse);  // 가짜 응답 반환
//
//    // 4. ImageUploadAdapter 객체 생성
//    ImageUploadAdapter imageUploadAdapter = new ImageUploadAdapter(restTemplate);  // 모킹한 RestTemplate 주입
//
//    // 5. 파일 업로드 메소드 호출
//    String result = imageUploadAdapter.uploadImage(mockFile);
//
//    // 6. 결과 확인
//    assertEquals("http://example.com/test-image.jpg", result);
//
//    // 7. RestTemplate의 exchange 메소드 호출 여부 확인
//    verify(restTemplate, times(1)).exchange(
//        any(URI.class), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class));
//  }


//  @Test
//  void testImageUploadWithPath() throws IOException {
//    // given
//    String fakeResponseBody = "{\"file\": {\"url\": \"http://example.com/test-image.jpg\"}}";
//    ResponseEntity<String> fakeResponse = new ResponseEntity<>(fakeResponseBody, HttpStatus.OK);
//    String testPath = "/test-path/";
//
//    // Mock RestTemplate's exchange method
//    when(restTemplate.exchange(
//        any(URI.class),
//        eq(HttpMethod.PUT),
//        any(HttpEntity.class),
//        eq(String.class)
//    )).thenReturn(fakeResponse);
//
//    // Prepare test file
//    MockMultipartFile testFile = new MockMultipartFile(
//        "imageFile", "test-image.jpg", MediaType.IMAGE_JPEG_VALUE, "test-image-content".getBytes()
//    );
//
//    // when
//    String result = imageUploadAdapter.uploadImage(testFile, testPath);
//
//    // then
//    assertEquals("http://example.com/test-image.jpg", result); // 업로드된 이미지 URL 확인
//    verify(restTemplate, times(1)).exchange(any(URI.class), eq(HttpMethod.PUT), any(HttpEntity.class), eq(String.class));
//  }


}
