package com.andrewbycode.cookdocs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public enum ImageTypeEnum {
    PROFILE(1,"Ảnh hồ sơ"),
    RECIPE(2,"Ảnh món ăn");

    private final Integer value;
    private final String name;

}
