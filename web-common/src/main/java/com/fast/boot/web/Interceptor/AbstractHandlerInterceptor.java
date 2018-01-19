package com.fast.boot.web.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by junqing.li on 17/4/18.
 */
@Slf4j
public abstract class AbstractHandlerInterceptor extends HandlerInterceptorAdapter {

  /**
   * 支持 HandlerMethod
   * <p>
   * 过滤css/js resourceHandler
   *
   * @param handler
   * @return
   */
  public final boolean supports(Object handler) {
    return handler instanceof HandlerMethod;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    if (supports(handler)) {

      return postHandle0(request, response, (HandlerMethod) handler);
    }

    return false;
  }

  public abstract boolean postHandle0(HttpServletRequest request, HttpServletResponse response,
      HandlerMethod handler);


  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {

  }

  @Override
  public void afterConcurrentHandlingStarted(HttpServletRequest request,
      HttpServletResponse response, Object handler) throws Exception {

  }
}
