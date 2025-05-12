package com.andrewbycode.cookdocs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClientRegistrationEnum {
    FACEBOOK(0,"facebook"),
    GOOGLE(1,"google");

    private final Integer code;
    private final String value;
}
