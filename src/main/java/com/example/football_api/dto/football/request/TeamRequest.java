package com.example.football_api.dto.football.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeamRequest {
    @Size(min = 3, max = 30)
    private String name;
}
