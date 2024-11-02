package com.swpgavf.back.exception;

import com.swpgavf.back.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionConfiguration {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDTO> handler(NotFoundException e) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(e.getMessage());
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);

    }

}

