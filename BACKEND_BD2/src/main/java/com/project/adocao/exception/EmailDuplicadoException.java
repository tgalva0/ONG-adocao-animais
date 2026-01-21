package com.project.adocao.exception;

public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String message) {
        super(message);
    }
}