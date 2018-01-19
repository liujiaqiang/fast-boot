package com.fast.boot.monitor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: junqing.li
 * @date: 17/8/15
 */
@Slf4j
@Order(1)
@Component
public class EmailMonitorHandler implements Monitor {

  @Autowired
  private EmailService emailService;

  @Value("${spring.application.name}")
  private String appName;

  @Value("${spring.mail.properties.rate}")
  private int rate;

  @Override
  public boolean support(MonitorContext context) {

    if (Strings.isNullOrEmpty(context.getUri())) {

      log.info("[support] uri is blank not to send mail");
      return false;
    }

    boolean enter = RateLimiterService.enter(context.getUri(), rate);

    if (!enter) {
      log.info("[support] send mail slack rate is limit uri={}", context.getUri());
    }
    return enter;
  }

  @Override
  public void handler(MonitorContext context) {

    Map<String, String> map = Maps.newLinkedHashMap();
    map.put("请求", context.getIp() + "-" + context.getUri());
    map.put("参数", context.getParams());
    map.put("机器", LocalHostUtils.getIp());
    map.put("环境", context.getProfile());
    map.put("堆栈", context.getException());

    emailService.sendHtmlMail(appName, map);
  }


}
