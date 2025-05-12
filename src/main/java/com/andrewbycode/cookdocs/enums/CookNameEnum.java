package com.andrewbycode.cookdocs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CookNameEnum {
    CHEF_CODE(0,"chefcode@");

    private final Integer code;
    private final String msg;
}
