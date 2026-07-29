package com.demo.error.handler;

import com.demo.enums.BizCodeEnum;
import com.demo.error.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
@Slf4j
@RestControllerAdvice
public class GlobalRestfulAPIExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value= BindException.class)
    public ResponseEntity<ApiError> handleValidException(BindException e){
        log.error("input validation errors: {}，exception type：{}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();

        Map<String,String> errorMap = new HashMap<>();
        Locale locale = LocaleContextHolder.getLocale();
        bindingResult.getFieldErrors().forEach((fieldError)->{
            String msg = messageSource.getMessage(fieldError, locale);
            errorMap.put(fieldError.getField(),msg);
        });
        ApiError error = new ApiError(
                BizCodeEnum.VALIDATION_EXCEPTION.getCode(),
                BizCodeEnum.VALIDATION_EXCEPTION.getMsg(),
                errorMap
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<ApiError> handleException(Throwable throwable){

        log.error("error：",throwable);
        ApiError error = new ApiError(
                BizCodeEnum.UNKNOW_EXCEPTION.getCode(),
                BizCodeEnum.VALIDATION_EXCEPTION.getMsg(),
                new HashMap<>()
        );
        return ResponseEntity.internalServerError().body(error);
    }

}
