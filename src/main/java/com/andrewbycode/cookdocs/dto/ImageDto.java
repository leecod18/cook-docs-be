package com.andrewbycode.cookdocs.dto;

import lombok.Data;

@Data
public class ImageDto {
    private Long id;
    private String fileName;
    private String filePath;
    private String fileContent;
    private String imageType;
}
