package com.linkedin.user_service.Dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
