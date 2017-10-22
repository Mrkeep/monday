package com.hyb.backstage.controller;

import com.hyb.common.util.bean.HybHttpResponse;
import com.hyb.common.util.error.ErrorCode;
import com.hyb.common.util.error.PreciseException;
import com.hyb.common.util.error.HybException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;

@ControllerAdvice
public class YBExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(YBExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(HybException.class)
    public HybHttpResponse handleYException(HybException e) {
        logger.error("YBException:{}", e.getErrorCode(), e);
        return HybHttpResponse.createFailedResponse(e.getErrorCode(), null);
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public HybHttpResponse handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("IllegalArgumentException:{}", e.getMessage(), e);
        return HybHttpResponse.createFailedResponse(ErrorCode.InvalidParameter, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(IllegalStateException.class)
    public HybHttpResponse handleIllegalStateException(IllegalStateException e) {
        logger.error("IllegalStateException:{}", e.getMessage(), e);
        return HybHttpResponse.createFailedResponse(ErrorCode.InternalServerError, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public HybHttpResponse handlerException(Exception e) {
        logger.error("Exception:{}", e.getMessage(), e);
        return HybHttpResponse.createFailedResponse(ErrorCode.UnknownError, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(PreciseException.class)
    public HybHttpResponse handlerPreciseException(PreciseException e) {
        logger.error("Exception:{}", e.getMessage(), e);
        return HybHttpResponse.createFailedResponse(e.getErrorCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HybHttpResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        Iterator errorIterator = e.getBindingResult().getAllErrors().iterator();

        while (errorIterator.hasNext()) {
            ObjectError error = (ObjectError) errorIterator.next();
            message.append(error.getDefaultMessage()).append("|");
        }
        if (StringUtils.isNotEmpty(message)) {
            message.deleteCharAt(message.length() - 1);
        }
        logger.error("Handle MethodArgumentNotValidException : {}", message.toString());
        return HybHttpResponse.createFailedResponseWithDescription(ErrorCode.InvalidParameter, message.toString());
    }
}
