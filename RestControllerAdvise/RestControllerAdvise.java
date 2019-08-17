package io.dtc.essearch.controller.advisesss;

import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.apache.commons.lang3.exception.ExceptionUtils;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestControllerAdvise {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ExceptionUtils.getStackTrace(ex));
        List<String> errors = new ArrayList<String>();
        
        ex.getBindingResult().getFieldErrors().stream().map( error -> {
            return errors.add(error.getField() + ": " + error.getDefaultMessage());
        }).count();

        return ResponseHelper.failResponse("Missing Data!", errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        log.error(ExceptionUtils.getStackTrace(ex));
        return ResponseHelper.failResponse(ex.getMessage(), ex);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleRestException(Exception ex) {
        log.error(ExceptionUtils.getStackTrace(ex));
        return ResponseHelper.failResponse(ex.getMessage(), ex);
    }
}
