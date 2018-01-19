package com.fast.boot.web.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fast.boot.web.context.ContextHolder;
import com.fast.boot.web.context.WebContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.WebUtils;

import com.fast.boot.web.Controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by junqing.li on 17/4/18.
 *
 * 上下文拦截器
 */
@Slf4j
public class ContextInterceptor extends AbstractHandlerInterceptor {

  @Getter
  @Setter
  @Value("${spring.web.debug:false}")
  private boolean debug;

  @Autowired
  private Environment environment;


  /**
   * 是否开启debug
   *
   * @param request
   * @return
   */
  protected boolean debug(HttpServletRequest request) {

    String debugParam = request.getParameter("debug");
    return "true".equals(debugParam) || debug;
  }

  @Override
  public boolean postHandle0(HttpServletRequest request, HttpServletResponse response,
      HandlerMethod handler) {

    boolean debug = debug(request);
    if (debug) {
      log.info("{} method={}, uri={}, params = {}", "ip", request.getMethod(),
          request.getRequestURI(), WebUtils.getParametersStartingWith(request, ""));
    }
    setLocalController(handler, debug);

    return true;
  }

  private void setLocalController(HandlerMethod handler, boolean debug) {
    boolean isTsmsController = Controller.class.isAssignableFrom(handler.getBeanType());
    WebContextHolder.setContextHolder(new ContextHolder(isTsmsController, debug));
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {

    WebContextHolder.resetContextHolder();
  }
}
