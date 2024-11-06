package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressRequest {

    @Size(max = 15)
    private String addressAlias; // 주소 별칭

    @NotBlank
    @Size(max = 35)
    private String addressDetail; //상세주소

    @NotBlank
    @Size(max = 30)
    private String addressRoadname; //도로명주소

    @NotBlank
    @Size(max = 5)
    private String addressPostalNumber; //우편번호
}
