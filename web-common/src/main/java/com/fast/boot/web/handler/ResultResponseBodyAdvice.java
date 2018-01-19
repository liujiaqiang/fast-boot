package com.fast.boot.web.handler;

import java.util.Objects;
import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 处理返回NULL
 * 
 */
@ControllerAdvice
public class ResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {


  public ResultResponseBodyAdvice() {}


  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {

    if (Objects.isNull(body)) {

      return Optional.ofNullable(body);
    }
    return body;
  }
}
