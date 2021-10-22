package com.yh20studio.springbootwebservice.controller;

import com.yh20studio.springbootwebservice.domain.exception.RestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
// 클래스에 선언하면 되며, 모든 @Controller에 대한, 전역적으로 발생할 수 있는 예외를 잡아서 처리할 수 있다.
// @ControllerAdvice와 @ResponseBody를 합쳐놓은 어노테이션이다. @ControllerAdvice와 동일한 역할을 수행하고, 추가적으로 @ResponseBody를 통해 객체를 리턴할 수도 있다.
public class ExceptionRestControllerAdvice {

    // Rest API 상황에서 Exception이 나왔을 때 핸들링하여 메세지를 직접 입력할 수 있도록 한다.
    @ExceptionHandler(RestException.class)
    public ResponseEntity<Map<String, Object>> handler(RestException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("message", e.getMessage());

        return new ResponseEntity<>(resBody, e.getStatus());
    }
}
