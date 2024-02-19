package com.geekster.wishlistservice.exceptionhandler;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * This POJO is a wrapper for an unsuccessful API request.
 * Object of this class shall be marshalled by Jackson and sent back to the client.
 */
@Data
@Builder
public class GenericErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timeStamp;
}
