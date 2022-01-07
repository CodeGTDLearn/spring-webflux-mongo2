package com.webflux.api.core.exception.modules;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ModulesExceptionAttributes {
//    private String title;
    private String detail;
    private String classType;
    private int status;
    private long timeStamp;
}