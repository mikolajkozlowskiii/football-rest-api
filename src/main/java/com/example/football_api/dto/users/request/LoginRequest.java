package com.example.football_api.dto.users.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
