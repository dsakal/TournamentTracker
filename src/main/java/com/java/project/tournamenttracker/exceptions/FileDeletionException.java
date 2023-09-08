package com.java.project.tournamenttracker.exceptions;

public class FileDeletionException extends RuntimeException{
    public FileDeletionException() {
    }

    public FileDeletionException(String message) {
        super(message);
    }

    public FileDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDeletionException(Throwable cause) {
        super(cause);
    }

    public FileDeletionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
