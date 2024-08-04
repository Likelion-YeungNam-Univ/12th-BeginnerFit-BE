package com.example.beginnerfitbe.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDto {
    private String email;
    private String code;
}

