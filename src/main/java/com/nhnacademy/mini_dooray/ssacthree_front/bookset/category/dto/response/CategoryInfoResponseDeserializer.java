package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryInfoResponseDeserializer extends JsonDeserializer<CategoryInfoResponse> {
    @Override
    public CategoryInfoResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // 필드 초기화
        long categoryId = 0;
        String categoryName = null;
        boolean categoryIsUsed = false;
        List<CategoryInfoResponse> children = new ArrayList<>();

        // JSON 시작 확인
        if (p.currentToken() == null) {
            p.nextToken();
        }
        if (p.currentToken() != JsonToken.START_OBJECT) {
            throw new RuntimeException("Expected START_OBJECT token, but got " + p.currentToken());
        }

        // JSON 필드 순회
        while (p.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = p.getCurrentName();
            p.nextToken();

            switch (fieldName) {
                case "categoryId":
                    categoryId = p.getLongValue();
                    break;
                case "categoryName":
                    categoryName = p.getValueAsString();
                    break;
                case "categoryIsUsed":
                    categoryIsUsed = p.getBooleanValue();
                    break;
                case "children":
                    if (p.currentToken() == JsonToken.START_ARRAY) {
                        while (p.nextToken() != JsonToken.END_ARRAY) {
                            CategoryInfoResponse child = deserialize(p, ctxt);
                            children.add(child);
                        }
                    }
                    break;
                default:
                    p.skipChildren();
            }
        }

        // CategoryInfoResponse 객체 생성
        return new CategoryInfoResponse(categoryId, categoryName, categoryIsUsed, children.isEmpty() ? new ArrayList<>() : children);
    }
}
