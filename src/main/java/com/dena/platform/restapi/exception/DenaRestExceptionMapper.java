package com.dena.platform.restapi.exception;

import com.dena.platform.common.exception.ErrorCode;
import com.dena.platform.restapi.dto.response.ErrorResponse;
import com.dena.platform.restapi.dto.response.ErrorResponse.ErrorResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
@ControllerAdvice
public class DenaRestExceptionMapper {
    private final static Logger log = LoggerFactory.getLogger(DenaRestExceptionMapper.class);

    @Resource(name = "denaMessageSource")
    private MessageSource messageSource;

    @ExceptionHandler(DenaRestException.class)
    @ResponseBody
    public ErrorResponse handleDenaRestException(HttpServletRequest request, HttpServletResponse response, DenaRestException ex) {
        response.setStatus(ex.getStatusCode());
        if (ex.getCause() != null) {
            log.error("An error occurred invoking a REST service.", ex.getCause());
        } else {
            log.error("An error occurred invoking a REST service.", ex);
        }

        final Locale locale = ex.getLocale() == null ? Locale.getDefault() : ex.getLocale();

        List<String> errorMessage = ex.getMessages().entrySet()
                .stream()
                .map(messageEntry -> messageSource.getMessage(messageEntry.getKey(), messageEntry.getValue(), locale))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponseBuilder.anErrorResponse()
                .withStatus(ex.getStatusCode())
                .withErrorCode(ex.getErrorCode())
                .withMessages(errorMessage)
                .build();

        return errorResponse;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public ErrorResponse handleDenaRestException(HttpServletRequest request, HttpServletResponse response, HttpMediaTypeNotSupportedException ex) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        final Locale locale = Locale.getDefault();

        String message = messageSource.getMessage(ErrorCode.INVALID_MEDIA_TYPE.getMessageCode(), null, locale);

        if (ex.getCause() != null) {
            log.error("An error occurred invoking a REST service.", ex.getCause());
        } else {
            log.error("An error occurred invoking a REST service.", ex);
        }

        ErrorResponse errorResponse = ErrorResponseBuilder.anErrorResponse()
                .withStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                .withErrorCode(ErrorCode.INVALID_MEDIA_TYPE.getErrorCode())
                .withMessages(message)
                .build();

        return errorResponse;
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ErrorResponse noHandlerFound404Exception(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException ex) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        final Locale locale = Locale.getDefault();

        String message = messageSource.getMessage(ErrorCode.RESOURCE_NOT_FOUND.getMessageCode(), null, locale);

        if (ex.getCause() != null) {
            log.error("An error occurred invoking a REST service.", ex.getCause());
        } else {
            log.error("An error occurred invoking a REST service.", ex);
        }

        ErrorResponse errorResponse = ErrorResponseBuilder.anErrorResponse()
                .withStatus(ErrorCode.RESOURCE_NOT_FOUND.getHttpStatusCode())
                .withErrorCode(ErrorCode.RESOURCE_NOT_FOUND.getErrorCode())
                .withMessages(message)
                .build();

        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorResponse handleDenaRestException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        final Locale locale = Locale.getDefault();

        String message = messageSource.getMessage(ErrorCode.GENERAL.getMessageCode(), null, locale);

        if (ex.getCause() != null) {
            log.error("An error occurred invoking a REST service.", ex.getCause());
        } else {
            log.error("An error occurred invoking a REST service.", ex);
        }

        ErrorResponse errorResponse = ErrorResponseBuilder.anErrorResponse()
                .withStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                .withErrorCode(ErrorCode.GENERAL.getErrorCode())
                .withMessages(message)
                .build();

        return errorResponse;
    }


}
