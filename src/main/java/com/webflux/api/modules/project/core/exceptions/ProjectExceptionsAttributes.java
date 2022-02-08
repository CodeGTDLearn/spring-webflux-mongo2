package com.webflux.api.modules.project.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ProjectExceptionsAttributes {
//    private String title;
    private String detail;
    private String classType;
    private int status;
    private long timeStamp;
}