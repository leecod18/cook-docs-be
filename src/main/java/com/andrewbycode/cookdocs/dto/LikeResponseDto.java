package com.andrewbycode.cookdocs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponseDto {
    private Long id;
    private String username;
    private Long recipeId;
}

