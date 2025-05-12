package com.andrewbycode.cookdocs.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long userId;
    private int starts;
    private String feedBack;
}
