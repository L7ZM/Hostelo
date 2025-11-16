package com.udev.hotel.controller.exceptionHandler;

public class PenaltyApplicableException extends RuntimeException {
    public PenaltyApplicableException(String message) {
        super(message);
    }
}