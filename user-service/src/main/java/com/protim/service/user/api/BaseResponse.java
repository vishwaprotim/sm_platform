package com.protim.service.user.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    String status;
    String message;

    public BaseResponse(HttpStatus httpStatus, String message){
        this.status = httpStatus.name();
        this.message = message;
    }

    public static class BaseResponseBuilder {
        // Lombok will latch into this overridden builder method
        public BaseResponseBuilder status(HttpStatus httpStatus) {
            if (httpStatus != null) {
                this.status = httpStatus.name();
            }
            return this;
        }
    }
}
