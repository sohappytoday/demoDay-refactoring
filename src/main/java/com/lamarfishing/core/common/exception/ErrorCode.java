package com.lamarfishing.core.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * common
     */
    INVALID_PUBLICId(HttpStatus.BAD_REQUEST, "Invalid publicId format"),
    INVALID_REQUEST_CONTENT(HttpStatus.BAD_REQUEST, "Invalid request content"),
    /**
     * user
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    INVALID_USER_GRADE(HttpStatus.BAD_REQUEST, "Invalid user grade"),
    /**
     * ship
     */
    SHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "Ship not found"),
    /**
     * schedule
     */
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "Schedule not found"),
    DUPLICATE_SCHEDULE(HttpStatus.BAD_REQUEST, "Duplicate schedule"),
    SCHEDULE_HAS_RESERVATIONS(HttpStatus.BAD_REQUEST, "Schedule has reservation"),
    INVALID_DEPARTURE_TIME(HttpStatus.BAD_REQUEST, "Invalid departure time"),
    INVALID_SCHEDULE_TYPE(HttpStatus.BAD_REQUEST, "Invalid schedule type"),
    UNAUTHORIZED_SCHEDULE_ACCESS(HttpStatus.UNAUTHORIZED, "Unauthorized schedule access"),
    INVALID_HEADCOUNT(HttpStatus.BAD_REQUEST, "Invalid headcount"),
    /**
     * reservation
     */
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Reservation not found"),
    /**
     * coupon
     */
    INVALID_COUPON_STATUS(HttpStatus.BAD_REQUEST, "Invalid coupon status"),
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "Coupon not found"),
    UNAUTHORIZED_COUPON_ACCESS(HttpStatus.UNAUTHORIZED, "Unauthorized coupon access"),
    /**
     * message
     */
    MESSAGE_SEND_FAILED(HttpStatus.BAD_GATEWAY, "Message send failed");


    private final HttpStatus status;
    private final String message;

}
