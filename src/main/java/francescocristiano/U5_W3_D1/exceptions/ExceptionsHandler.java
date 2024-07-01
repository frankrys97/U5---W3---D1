package francescocristiano.U5_W3_D1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public NewErrorsDTO handleBadRequest(BadRequestException ex) {
        return new NewErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NewErrorsDTO handleNotFound(NotFoundException ex) {
        return new NewErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public NewErrorsDTO handleGenericErrors(Exception ex) {
        ex.printStackTrace();
        return new NewErrorsDTO("Something went wrong with server, we are working on it", LocalDateTime.now());
    }
}
