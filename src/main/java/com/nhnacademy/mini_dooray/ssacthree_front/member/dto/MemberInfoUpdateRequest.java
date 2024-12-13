package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class MemberInfoUpdateRequest {
    
    @NotBlank(message = "공백일 수 없습니다.")
    @Size(max = 10, message = "최대 10글자만 입력할 수 있습니다.")
    // 10글자만 주기로 함.
    private String customerName;

    @NotBlank(message = "공백일 수 없습니다.")
    @Size(max = 11)
    @Pattern(regexp = "^\\d{11}$", message = "전화번호는 하이픈 없이 11자리 숫자여야 합니다.")
    private String customerPhoneNumber;

    @NotBlank(message = "공백일 수 없습니다.")
    @Size(max = 50)
    @Pattern(regexp = "^\\w+@[\\w.-]+\\.[A-Za-z]{2,6}$", message = "올바른 이메일 주소를 입력하세요.")
    private String customerEmail;


}
