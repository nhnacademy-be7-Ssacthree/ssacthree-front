package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberRegisterRequest {


    @NotBlank(message = "공백일 수 없습니다.")
    @Size(min = 5, max = 20, message = "5~20글자 사이로 입력해야합니다.")
    @Pattern(regexp = "^[A-Za-z0-9]{5,20}$", message = "아이디는 영어와 숫자로 5자에서 20자 사이로 입력하십시오.")
    private String loginId;

    @NotBlank(message = "공백일 수 없습니다.")
    @Size(min = 8, max = 20, message = "8~20글자 사이로 입력해야합니다.")
    //비밀번호는 8글자에서 20글자만 받자!
    private String loginPassword;

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

    @NotBlank(message = "공백일 수 없습니다.")
    @Size(min = 8, max = 8, message = "생년월일은 숫자 8자리여야 합니다.")
    @Pattern(regexp = "^\\d{8}$", message = "8자리 숫자로만 입력 가능합니다.")
    private String birth;

    public void setBirth(String birth) {
        if (birth != null) {
            this.birth = birth.replace("-", "");
        }
    }


}
