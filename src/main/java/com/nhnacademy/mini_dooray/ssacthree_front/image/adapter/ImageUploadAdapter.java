package com.nhnacademy.mini_dooray.ssacthree_front.image.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class ImageUploadAdapter {

    private static final String API_URL = "https://api-image.nhncloudservice.com/image/v2.0/appkeys/rUN43QEwj1P6jThk/images";
    private static final String SECRET_KEY = "I1XLVufp";
    private static String imagePath = "/ssacthree/packaging/"; // 업로드할 경로와 파일명 지정

    public static String updateImagePath(String imagePath, ByteArrayResource imageResource) {
        return imagePath + imageResource.getFilename();
    }

    public String uploadImage(MultipartFile imageFile) {
        try {
            // Prepare the HTTP request headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", SECRET_KEY);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            // 이미지 파일의 바이너리 데이터를 ByteArrayResource로 변환
            ByteArrayResource imageResource = new ByteArrayResource(imageFile.getBytes()) {
                @Override
                public String getFilename() {
                    return imageFile.getOriginalFilename();
                }
            };

            // URL에 경로와 옵션을 추가
            String newImagePath = updateImagePath(imagePath, imageResource);
            String encodedPath = URLEncoder.encode(newImagePath, StandardCharsets.UTF_8);
            String uploadUrl = API_URL + "?path=" + encodedPath + "&overwrite=true";

            // HttpEntity 생성
            HttpEntity<ByteArrayResource> entity = new HttpEntity<>(imageResource, headers);

            // RestTemplate을 사용하여 업로드 요청 전송
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    URI.create(uploadUrl), HttpMethod.PUT, entity, String.class);

            // JSON 파싱하여 file.url 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode fileUrlNode = rootNode.path("file").path("url");

            return fileUrlNode.asText(); // file.url 값만 반환
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed due to IOException.";
        } catch (Exception e) {
            e.printStackTrace();
            return "File upload failed due to an error: " + e.getMessage();
        }
    }

    public String uploadImage(MultipartFile imageFile, String path) {
        try {
            // Prepare the HTTP request headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", SECRET_KEY);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            // 이미지 파일의 바이너리 데이터를 ByteArrayResource로 변환
            ByteArrayResource imageResource = new ByteArrayResource(imageFile.getBytes()) {
                @Override
                public String getFilename() {
                    return imageFile.getOriginalFilename();
                }
            };

            // URL에 경로와 옵션을 추가
            path = path + imageResource.getFilename();
            String encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8.toString());
            String uploadUrl = API_URL + "?path=" + encodedPath + "&overwrite=true";

            // HttpEntity 생성
            HttpEntity<ByteArrayResource> entity = new HttpEntity<>(imageResource, headers);

            // RestTemplate을 사용하여 업로드 요청 전송
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                URI.create(uploadUrl), HttpMethod.PUT, entity, String.class);

            // JSON 파싱하여 file.url 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode fileUrlNode = rootNode.path("file").path("url");

            return fileUrlNode.asText(); // file.url 값만 반환
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed due to IOException.";
        } catch (Exception e) {
            e.printStackTrace();
            return "File upload failed due to an error: " + e.getMessage();
        }
    }
}
