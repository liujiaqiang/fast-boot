package com.fast.boot.monitor;

import lombok.Data;

/**
 * @author: junqing.li
 * @date: 17/8/15
 */
@Data
public class MonitorContext {

  private String ip;

  private String uri;

  private String params;

  private String method;

  private String profile;

  private String exception;


}
