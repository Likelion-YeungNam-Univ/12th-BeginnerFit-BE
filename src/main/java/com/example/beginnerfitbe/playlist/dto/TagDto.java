package com.example.beginnerfitbe.playlist.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagDto {
    private String tag;
    public TagDto(String tag) {
        this.tag = tag;
    }

}
