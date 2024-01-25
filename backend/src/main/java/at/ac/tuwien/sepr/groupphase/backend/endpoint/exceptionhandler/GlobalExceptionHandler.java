package at.ac.tuwien.sepr.groupphase.backend.endpoint.exceptionhandler;

import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.exception.ErrorListExceptionDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.exception.SingleExceptionDto;
import at.ac.tuwien.sepr.groupphase.backend.endpoint.dto.exception.BlockedIpExceptionDto;
import at.ac.tuwien.sepr.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ErrorListException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotAuthorizedException;
import at.ac.tuwien.sepr.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepr.groupphase.backend.exception.PaypalException;
import at.ac.tuwien.sepr.groupphase.backend.exception.SingleException;
import at.ac.tuwien.sepr.groupphase.backend.exception.BlockedIpException;
import at.ac.tuwien.sepr.groupphase.backend.exception.ValidationException;
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
 * GlobalExceptionHandler class handles and maps exceptions to HTTP responses for all controllers.
 * It extends ResponseEntityExceptionHandler to provide default handling for Spring MVC exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Handles NotFoundException and maps it to HTTP 404 Not Found response.
     *
     * @param e       The NotFoundException.
     * @param request The WebRequest.
     * @return ResponseEntity with HTTP 404 status and a SingleExceptionDto containing error details.
     */
    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(SingleException e, WebRequest request) {

        SingleExceptionDto responseBody = new SingleExceptionDto()
            .setMessage(e.getMessage())
            .setUiError(e.uiError());

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles ValidationException and maps it to HTTP 422 Unprocessable Entity response.
     *
     * @param e       The ValidationException.
     * @param request The WebRequest.
     * @return ResponseEntity with HTTP 422 status and an ErrorListExceptionDto containing validation error details.
     */
    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ErrorListException e, WebRequest request) {

        ErrorListExceptionDto responseBody = new ErrorListExceptionDto()
            .setUiErrorDescriptor(e.uiErrorSummary())
            .setErrors(e.errors())
            .setUiErrors(e.uiErrors());

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    /**
     * Handles DataAccessException and maps it to HTTP 500 Internal Server Error response.
     *
     * @param ex      The DataAccessException.
     * @param request The WebRequest.
     * @return ResponseEntity with HTTP 500 status and the error message.
     */
    @ExceptionHandler(value = {DataAccessException.class})
    protected ResponseEntity<Object> handleDataAccessException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handles NotAuthorizedException and maps it to HTTP 401 Unauthorized response.
     *
     * @param e       The NotAuthorizedException.
     * @param request The WebRequest.
     * @return ResponseEntity with HTTP 401 status and a SingleExceptionDto containing error details.
     */
    @ExceptionHandler(value = {NotAuthorizedException.class})
    protected ResponseEntity<Object> handleNotAuthorizedException(SingleException e, WebRequest request) {

        SingleExceptionDto responseBody = new SingleExceptionDto()
            .setMessage(e.getMessage())
            .setUiError(e.uiError());

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * Handles ConflictException and maps it to HTTP 409 Conflict response.
     *
     * @param e       The ConflictException.
     * @param request The WebRequest.
     * @return ResponseEntity with HTTP 409 status and an ErrorListExceptionDto containing conflict error details.
     */
    @ExceptionHandler(value = {ConflictException.class})
    protected ResponseEntity<Object> handleConflictException(ErrorListException e, WebRequest request) {

        ErrorListExceptionDto responseBody = new ErrorListExceptionDto()
            .setUiErrorDescriptor(e.uiErrorSummary())
            .setErrors(e.errors())
            .setUiErrors(e.uiErrors());

        return handleExceptionInternal(e, responseBody, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Handles BlockedIpException and maps it to HTTP 401 Unauthorized response.
     *
     * @param e       The BlockedIpException.
     * @param request The WebRequest.
     * @return ResponseEntity with HTTP 401 status and a BlockedIpExceptionDto containing error details.
     */
    @ExceptionHandler(value = {BlockedIpException.class})
    protected ResponseEntity<Object> handleTimoutException(BlockedIpException e, WebRequest request) {
        BlockedIpExceptionDto dto = (BlockedIpExceptionDto) new BlockedIpExceptionDto()
            .setTimeout(e.getTimeout())
            .setUiError("TE0")
            .setMessage("Action hast timeout for user");
        return handleExceptionInternal(e, dto, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    /**
     * Handles PaypalException and maps it to HTTP 503 Service Unavailable response.
     *
     * @param e       The PaypalException.
     * @param request The WebRequest.
     * @return ResponseEntity with HTTP 503 status and the error message.
     */
    @ExceptionHandler(value = {PaypalException.class})
    protected ResponseEntity<Object> handlePaypalException(PaypalException e, WebRequest request) {
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    // TODO handle IOException.

    /**
     * Overrides methods from ResponseEntityExceptionHandler to send a customized HTTP response for a known exception
     * from e.g. Spring.
     *
     * @param ex      The MethodArgumentNotValidException.
     * @param headers The HttpHeaders.
     * @param status  The HTTP status.
     * @param request The WebRequest.
     * @return ResponseEntity with a map containing validation errors.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        // Get all errors
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + " " + err.getDefaultMessage())
            .collect(Collectors.toList());
        body.put("Validation errors", errors);

        return new ResponseEntity<>(body.toString(), headers, status);

    }
}
