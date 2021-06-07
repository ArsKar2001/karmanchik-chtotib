package karmanchik.chtotib.restservice.advice;

import karmanchik.chtotib.entityservice.exception.StringReadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StringReadExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(StringReadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String stringReadException(StringReadException e) {
        return e.getMessage();
    }
}
