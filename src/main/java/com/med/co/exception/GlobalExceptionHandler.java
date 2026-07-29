package com.med.co.exception;


import java.util.HashMap;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import com.med.co.dto.response.ApiResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {



    // Resource Not Found Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(
            ResourceNotFoundException ex) {


        ApiResponse<?> response =
                new ApiResponse<>(
                        404,
                        ex.getMessage(),
                        null
                );


        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
        );
    }





    // Bad Request Exception
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequestException(
            BadRequestException ex) {


        ApiResponse<?> response =
                new ApiResponse<>(
                        400,
                        ex.getMessage(),
                        null
                );


        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }





    // Duplicate Resource Exception
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException ex) {


        ApiResponse<?> response =
                new ApiResponse<>(
                        409,
                        ex.getMessage(),
                        null
                );


        return new ResponseEntity<>(
                response,
                HttpStatus.CONFLICT
        );
    }





    // Validation Exception (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
            MethodArgumentNotValidException ex) {



        Map<String, String> errors =
                new HashMap<>();



        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {


                    errors.put(
                            error.getField(),
                            error.getDefaultMessage()
                    );

                });




        ApiResponse<?> response =
                new ApiResponse<>(
                        400,
                        "Validation failed",
                        errors
                );



        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }





    // Handle All Other Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(
            Exception ex) {


        ApiResponse<?> response =
                new ApiResponse<>(
                        500,
                        "Something went wrong",
                        ex.getMessage()
                );



        return new ResponseEntity<>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}