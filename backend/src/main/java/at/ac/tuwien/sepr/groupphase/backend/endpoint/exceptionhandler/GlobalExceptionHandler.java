package at.ac.tuwien.sepr.groupphase.backend.endpoint.exceptionhandler;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.exception.ErrorListExceptionDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.exception.SingleExceptionDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ErrorListException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.PaypalException;
import at.ac.tuwien.sepr.groupphase.backend.exception.SingleException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
import jakarta.persistence.ElementCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Register all your Java exceptions here to map them into meaningful HTTP exceptions.
 * If you have special cases which are only important for specific endpoints, use ResponseStatusExceptions
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // TODO doku
    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(SingleException e, WebRequest request) {

        SingleExceptionDto responseBody = new SingleExceptionDto()
            .setMessage(e.getMessage())
            .setUiError(e.uiError());

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }


    // TODO doku
    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ErrorListException e, WebRequest request) {

        ErrorListExceptionDto responseBody = new ErrorListExceptionDto()
            .setUiErrorDescriptor(e.uiErrorSummary())
            .setErrors(e.errors())
            .setUiErrors(e.uiErrors());

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    // TODO doku
    @ExceptionHandler(value = {DataAccessException.class})
    protected ResponseEntity<Object> handleDataAccessException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handles NotAuthorizedException thrown in the REST Controller.
     *
     * @param e       RuntimeException cached from REST Controller
     * @param request the initial {@link WebRequest}
     * @return {@link ResponseEntity} with HttpStatus.UNAUTHORIZED
     */
    @ExceptionHandler(value = {NotAuthorizedException.class})
    protected ResponseEntity<Object> handleNotAuthorizedException(SingleException e, WebRequest request) {

        SingleExceptionDto responseBody = new SingleExceptionDto()
            .setMessage(e.getMessage())
            .setUiError(e.uiError());

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = {ConflictException.class})
    protected ResponseEntity<Object> handleConflictException(ErrorListException e, WebRequest request) {

        ErrorListExceptionDto responseBody = new ErrorListExceptionDto()
            .setUiErrorDescriptor(e.uiErrorSummary())
            .setErrors(e.errors())
            .setUiErrors(e.uiErrors());

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {PaypalException.class})
    protected ResponseEntity<Object> handlePaypalException(PaypalException e, WebRequest request) {
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }
    // TODO handle IOException.

    /**
     * Override methods from ResponseEntityExceptionHandler to send a customized HTTP response for a know exception
     * from e.g. Spring
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        //Get all errors
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + " " + err.getDefaultMessage())
            .collect(Collectors.toList());
        body.put("Validation errors", errors);

        return new ResponseEntity<>(body.toString(), headers, status);

    }
}
