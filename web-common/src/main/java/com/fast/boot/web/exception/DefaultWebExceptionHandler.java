package com.fast.boot.web.exception;

import javax.servlet.http.HttpServletRequest;

import com.fast.boot.web.JsonUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.event.Level;

import com.fast.boot.api.BootResult;
import com.fast.boot.api.exception.BootBizException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: junqing.li
 * @date: 17/7/29
 */
@Slf4j
public class DefaultWebExceptionHandler implements WebExceptionHandler {

  @Override
  public void log(String callMethod, Exception e, HttpServletRequest request, Level level) {

    StringBuilder logFomart = new StringBuilder();
    logFomart.append(callMethod);
    logFomart.append(" request ip={} method={} param={} exception={}");

    String ip = getIp();

    String logFormatStr = logFomart.toString();
    String param = JsonUtils.toJSON(request.getParameterMap());
    String exception = ExceptionUtils.getStackTrace(e);

    // 是Groot异常不要打印堆栈信息
    if (BootBizException.class.isAssignableFrom(e.getClass())) {
      BootBizException bizException = (BootBizException) e;
      exception = "code=" + bizException.getCode() + " msg=" + bizException.getMsg();
    }

    // 不记录堆栈
    if (IllegalArgumentException.class.isAssignableFrom(e.getClass())) {
      exception = e.getMessage();
    }

    String uri = request.getRequestURI();

    beforeLog(e, request, level);

    switch (level) {
      case DEBUG:
        log.debug(logFormatStr, ip, uri, param, exception);
        break;

      case INFO:
        log.info(logFormatStr, ip, uri, param, exception);
        break;

      case WARN:
        log.warn(logFormatStr, ip, uri, param, exception);
        break;

      case ERROR:
        log.error(logFormatStr, ip, uri, param, exception);
        break;

      case TRACE:
        log.trace(logFormatStr, ip, uri, param, exception);
        break;

    }

    afterLog(e, request, level);

  }

  protected void beforeLog(Exception e, HttpServletRequest request, Level level) {

  }

  /**
   * 记录日志之后的行为
   *
   * @param e
   * @param request
   * @param level
   */
  protected void afterLog(Exception e, HttpServletRequest request, Level level) {

  }

  @Override
  public Object fail(String code, String msg) {

    return BootResult.fail(code, msg);
  }

  public String getIp() {
    return "";
  }
}
