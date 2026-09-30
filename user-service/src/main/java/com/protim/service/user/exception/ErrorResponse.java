package com.protim.service.user.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    String status;
    String message;

    public ErrorResponse(HttpStatus httpStatus, String message){
        this.status = httpStatus.name();
        this.message = message;
    }

    public static class ErrorResponseBuilder {
        // Lombok will latch into this overridden builder method
        public ErrorResponseBuilder status(HttpStatus httpStatus) {
            if (httpStatus != null) {
                this.status = httpStatus.name();
            }
            return this;
        }
    }
}
