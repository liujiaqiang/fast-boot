package com.fast.boot.web.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.event.Level;

/**
 * @author: junqing.li
 * @date: 17/7/29
 */
public interface WebExceptionHandler {


  /**
   * log处理方式
   * 
   * @param callMethod
   * @param e
   * @param request
   * @param level
   */
  void log(String callMethod, Exception e, HttpServletRequest request, Level level);

  /**
   *
   * 通用放回结果处理
   *
   * @param code
   * @param msg
   * @return
   */
  Object fail(String code, String msg);
}
