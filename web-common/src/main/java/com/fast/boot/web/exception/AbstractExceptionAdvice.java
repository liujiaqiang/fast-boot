package com.fast.boot.web.exception;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fast.boot.api.BootCode;
import com.fast.boot.api.exception.ArgumentBizException;
import com.fast.boot.api.exception.BootBizException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 通用异常处理
 * 
 * @author: junqing.li
 * @date: 17/7/29
 */
@Slf4j
public class AbstractExceptionAdvice {


  @Getter
  @Setter
  @Autowired
  private WebExceptionHandler webExceptionHandler;

  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler({SQLException.class, Exception.class})
  public Object handlerException(Exception e, HttpServletRequest request) {

    webExceptionHandler.log("[handlerException]", e, request, Level.ERROR);

    return webExceptionHandler.fail(BootCode.SYS_ERROR, "系统异常");
  }

  /**
   * 业务异常
   *
   * @param e
   * @param request
   * @return
   */
  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(BootBizException.class)
  public Object bizException(BootBizException e, HttpServletRequest request) {

    webExceptionHandler.log("[illegalArgumentException]", e, request, Level.WARN);

    return webExceptionHandler.fail(e.getCode(), e.getMsg());
  }


  /**
   * java标准参数异常
   *
   * @param e
   * @param request
   * @return
   */
  @ResponseStatus(HttpStatus.OK)
  @ExceptionHandler(IllegalArgumentException.class)
  public Object illegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {

    webExceptionHandler.log("[illegalArgumentException]", e, request, Level.INFO);

    return webExceptionHandler.fail(BootCode.ARGUMENT_ERROR, e.getMessage());
  }


  /**
   * spring validator 异常
   *
   * @param e
   * @param request
   * @return
   */
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.OK)
  public Object bindException(BindException e, HttpServletRequest request) {

    webExceptionHandler.log("[bindException]", e, request, Level.INFO);

    StringBuilder result = new StringBuilder();
    List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
    result.append("参数错误[");
    for (FieldError fieldError : fieldErrorList) {
      result.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage())
          .append(",");
    }
    result.deleteCharAt(result.length() - 1);
    result.append("]");
    return webExceptionHandler.fail(BootCode.ARGUMENT_ERROR, result.toString());
  }

  /**
   * java bean validate for 业务层 异常
   *
   * @param e
   * @param request
   * @return
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.OK)
  public Object constraintViolationException(ConstraintViolationException e,
      HttpServletRequest request) {

    webExceptionHandler.log("[constraintViolationException]", e, request, Level.INFO);

    StringBuilder result = new StringBuilder();
    result.append("参数错误[");
    Set<ConstraintViolation<?>> sets = e.getConstraintViolations();
    for (ConstraintViolation constrainViolation : sets) {
      PathImpl path = (PathImpl) constrainViolation.getPropertyPath();
      result.append(path.getLeafNode().asString()).append(":")
          .append(constrainViolation.getMessage()).append(",");
    }
    result.deleteCharAt(result.length() - 1);
    result.append("]");
    return webExceptionHandler.fail(BootCode.ARGUMENT_ERROR, result.toString());
  }

  /**
   * 参数异常处理
   */
  @ExceptionHandler({MissingServletRequestParameterException.class,
      ServletRequestBindingException.class, HttpRequestMethodNotSupportedException.class})
  @ResponseStatus(HttpStatus.OK)
  Object handleMissingServletRequestParameterException(Exception e, HttpServletRequest request) {

    webExceptionHandler.log("[handleMissingServletRequestParameterException]", e, request,
        Level.INFO);

    return webExceptionHandler.fail(BootCode.ARGUMENT_ERROR, "参数异常");
  }

  /**
   * http meida erro
   */
  @ExceptionHandler({HttpMediaTypeNotSupportedException.class,
      HttpMediaTypeNotAcceptableException.class})
  @ResponseStatus(HttpStatus.OK)
  Object handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e,
      HttpServletRequest request) {

    webExceptionHandler.log("[handleHttpMediaTypeNotSupportedException]", e, request, Level.INFO);


    return webExceptionHandler.fail(BootCode.ARGUMENT_ERROR, "请求类型异常");
  }

  @ExceptionHandler(org.springframework.beans.TypeMismatchException.class)
  @ResponseStatus(HttpStatus.OK)
  Object typeMismatchException(Exception e, HttpServletRequest request) {

    webExceptionHandler.log("[typeMismatchException]", e, request, Level.INFO);

    return webExceptionHandler.fail(BootCode.ARGUMENT_ERROR, "请求类型异常");
  }

  @ExceptionHandler(ArgumentBizException.class)
  @ResponseStatus(HttpStatus.OK)
  Object handleArgumentBizException(ArgumentBizException e, HttpServletRequest request) {

    webExceptionHandler.log("[handleArgumentBizException]", e, request, Level.INFO);

    return webExceptionHandler.fail(BootCode.ARGUMENT_ERROR, e.getMsg());
  }



  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.OK)
  Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
      HttpServletRequest request) {

    webExceptionHandler.log("[handleMethodArgumentNotValidException]", e, request, Level.INFO);

    List<ObjectError> errors = e.getBindingResult().getAllErrors();
    StringBuffer errorMsg = new StringBuffer();
    errors.stream().forEach(x -> errorMsg.append(x.getDefaultMessage()).append(";"));
    return webExceptionHandler.fail(BootCode.ARGUMENT_ERROR, errorMsg.toString());
  }



}
