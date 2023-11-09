package br.com.project.samuraicars.exception;

public class EmailAlreadyExsistException extends RuntimeException{
    public EmailAlreadyExsistException(String message) {
        super(message);
    }
}
