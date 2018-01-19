package com.fast.boot.monitor;

/**
 * @author: junqing.li
 * @date: 17/8/15
 */
public interface Monitor {

  boolean support(MonitorContext context);

  void handler(MonitorContext context);

}
