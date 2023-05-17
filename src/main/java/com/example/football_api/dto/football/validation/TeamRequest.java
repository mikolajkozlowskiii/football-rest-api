package com.example.football_api.dto.football.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequest {
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;
}
