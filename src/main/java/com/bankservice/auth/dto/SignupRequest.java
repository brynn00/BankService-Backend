package com.bankservice.auth.dto;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Pattern(regexp = "^[가-힣]{2,10}$")
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\d{6}-\\d{7}$")
    private String residentNumber;

    @NotBlank
    @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$")
    private String phone;

    @NotBlank
    @Size(max = 255)
    private String address;
}
