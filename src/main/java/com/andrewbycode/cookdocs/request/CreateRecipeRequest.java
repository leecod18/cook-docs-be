package com.andrewbycode.cookdocs.request;

import com.andrewbycode.cookdocs.entitys.Recipe;
import lombok.Data;

@Data
public class CreateRecipeRequest {
    private Recipe recipe;
    private Long userId;
}

