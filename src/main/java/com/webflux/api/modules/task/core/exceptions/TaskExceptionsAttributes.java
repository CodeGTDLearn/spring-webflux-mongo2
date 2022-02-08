package com.webflux.api.modules.task.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class TaskExceptionsAttributes {
//    private String title;
    private String detail;
    private String classType;
    private int status;
    private long timeStamp;
}