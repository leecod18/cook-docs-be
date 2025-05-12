package com.andrewbycode.cookdocs.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String userName;
    private String email;
    private String clientRegistration;
}
