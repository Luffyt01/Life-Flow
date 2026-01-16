package com.project.user_service.exception.ExceptionType;

public class MailSendingErrorException extends RuntimeException {

    public MailSendingErrorException(String message) {
        super(message);
    }

    public MailSendingErrorException() {
    }
}
