package com.peko.houshoukaizokudan.handler;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value=MaxUploadSizeExceededException.class)
    public String imageSizeHandler(Model model) {
        model.addAttribute("errorMsg", "sjhgsj");
        return null;
    }


    // 捕獲所有其他異常
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        // 處理異常的邏輯
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}