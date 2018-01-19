package com.fast.boot.web.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Objects;

import com.fast.boot.web.context.WebContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.fast.boot.api.BootResult;

/**
 * 自定义最终返回结果
 */
public class ResultHttpMessageConverter extends AbstractHttpMessageConverter<Object>
    implements GenericHttpMessageConverter<Object> {

  /**
   * with fastJson config
   */
  private FastJsonConfig fastJsonConfig = new FastJsonConfig();

  /**
   * @return the fastJsonConfig.
   * @since 1.2.11
   */
  public FastJsonConfig getFastJsonConfig() {
    return fastJsonConfig;
  }

  /**
   * @param fastJsonConfig the fastJsonConfig to set.
   * @since 1.2.11
   */
  public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
    this.fastJsonConfig = fastJsonConfig;
  }

  /**
   * Can serialize/deserialize all types.
   */
  public ResultHttpMessageConverter() {

    super(MediaType.ALL);
  }

  @Override
  protected boolean supports(Class<?> clazz) {

    return true;
  }

  @Override
  protected Object readInternal(Class<? extends Object> clazz, //
      HttpInputMessage inputMessage //
  ) throws IOException, HttpMessageNotReadableException {

    InputStream in = inputMessage.getBody();
    return JSON.parseObject(in, fastJsonConfig.getCharset(), clazz, fastJsonConfig.getFeatures());
  }

  @Override
  protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {
    HttpHeaders headers = outputMessage.getHeaders();
    ByteArrayOutputStream outnew = new ByteArrayOutputStream();

    boolean writeAsToString = false;

    // 封装返回类型
    Object result = wrapper(obj);

    if (result != null) {
      String className = result.getClass().getName();
      if ("com.fasterxml.jackson.databind.node.ObjectNode".equals(className)) {
        writeAsToString = true;
      }
    }

    if (writeAsToString) {
      String text = result.toString();
      OutputStream out = outputMessage.getBody();
      out.write(text.getBytes());
      if (fastJsonConfig.isWriteContentLength()) {
        headers.setContentLength(text.length());
      }
    } else {
      int len = JSON.writeJSONString(outnew, //
          fastJsonConfig.getCharset(), //
          result, //
          fastJsonConfig.getSerializeConfig(), //
          fastJsonConfig.getSerializeFilters(), //
          fastJsonConfig.getDateFormat(), //
          JSON.DEFAULT_GENERATE_FEATURE, //
          fastJsonConfig.getSerializerFeatures());
      if (fastJsonConfig.isWriteContentLength()) {
        headers.setContentLength(len);
      }

      OutputStream out = outputMessage.getBody();
      outnew.writeTo(out);
    }

    outnew.close();
  }

  /**
   * 包装 service返回过来的对象
   */
  private Object wrapper(Object obj) {

    // 非本地Controller 不处理
    if (Objects.isNull(WebContextHolder.getContextHolder())
        || !WebContextHolder.getContextHolder().isLocalController()) {
      return obj;
    }

    Object result = obj;
    if (result instanceof BootResult) {

    } else {
      result = BootResult.success(obj);
    }
    return result;
  }

  /*
   * @see
   * org.springframework.http.converter.GenericHttpMessageConverter#canRead(java.lang.reflect.Type,
   * java.lang.Class, org.springframework.http.MediaType)
   */
  public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
    return super.canRead(contextClass, mediaType);
  }

  /*
   * @see
   * org.springframework.http.converter.GenericHttpMessageConverter#canWrite(java.lang.reflect.Type,
   * java.lang.Class, org.springframework.http.MediaType)
   */
  public boolean canWrite(Type type, Class<?> contextClass, MediaType mediaType) {
    return super.canWrite(contextClass, mediaType);
  }

  /*
   * @see
   * org.springframework.http.converter.GenericHttpMessageConverter#read(java.lang.reflect.Type,
   * java.lang.Class, org.springframework.http.HttpInputMessage)
   */
  public Object read(Type type, //
      Class<?> contextClass, //
      HttpInputMessage inputMessage //
  ) throws IOException, HttpMessageNotReadableException {

    InputStream in = inputMessage.getBody();
    return JSON.parseObject(in, fastJsonConfig.getCharset(), type, fastJsonConfig.getFeatures());
  }

  /*
   * @see org.springframework.http.converter.GenericHttpMessageConverter#write(java.lang.Object,
   * java.lang.reflect.Type, org.springframework.http.MediaType,
   * org.springframework.http.HttpOutputMessage)
   */
  public void write(Object t, //
      Type type, //
      MediaType contentType, //
      HttpOutputMessage outputMessage //
  ) throws IOException, HttpMessageNotWritableException {

    HttpHeaders headers = outputMessage.getHeaders();
    if (headers.getContentType() == null) {
      if (contentType == null || contentType.isWildcardType() || contentType.isWildcardSubtype()) {
        contentType = getDefaultContentType(t);
      }
      if (contentType != null) {
        headers.setContentType(contentType);
      }
    }
    if (headers.getContentLength() == -1) {
      Long contentLength = getContentLength(t, headers.getContentType());
      if (contentLength != null) {
        headers.setContentLength(contentLength);
      }
    }
    writeInternal(t, outputMessage);
    outputMessage.getBody().flush();
  }

}
