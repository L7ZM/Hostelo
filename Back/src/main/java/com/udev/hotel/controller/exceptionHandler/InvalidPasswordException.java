package com.udev.hotel.controller.exceptionHandler;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;


public class InvalidPasswordException extends AbstractThrowableProblem {

    public InvalidPasswordException() {
        super(ErrorConstants.INVALID_PASSWORD_TYPE, "Incorrect password", Status.BAD_REQUEST);
    }
}
