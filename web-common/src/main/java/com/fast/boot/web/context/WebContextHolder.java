package com.fast.boot.web.context;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Created by junqing.li on 17/4/28.
 *
 * web 上下文
 */
public class WebContextHolder extends RequestContextHolder {


  private final static ThreadLocal<ContextHolder> LOCAL_CONTEXT = new ThreadLocal<>();

  /**
   * 设置 session
   */
  public static void setSessionAttributes(String name, Object object) {

    currentRequestAttributes().setAttribute(name, object, RequestAttributes.SCOPE_GLOBAL_SESSION);
  }

  public static void setContextHolder(ContextHolder holder) {

    LOCAL_CONTEXT.set(holder);
  }

  public static ContextHolder getContextHolder() {
    return LOCAL_CONTEXT.get();
  }

  public static void resetContextHolder() {

    LOCAL_CONTEXT.remove();
  }

}
