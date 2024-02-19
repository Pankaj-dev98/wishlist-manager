package com.geekster.wishlistservice.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Validated
public class GenericSuccessResponse<T> {
    private String message;
    private T target;
    private LocalDateTime timeStamp;
    private int status;
}