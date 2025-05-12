package com.andrewbycode.cookdocs.request;


import lombok.Data;

@Data
public class ImageRequest {
    private String fileName;
    private String fileContent;
    private int imageType;
    private Long recipeId;
    private Long userId;
}
