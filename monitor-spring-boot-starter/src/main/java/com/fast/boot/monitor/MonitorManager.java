package com.fast.boot.monitor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


/**
 * 报警管理
 * 
 * @author: junqing.li
 * @date: 17/8/15
 */
@Component
public class MonitorManager {


  @Autowired
  private List<Monitor> monitorList;


  @Async
  public void slack(MonitorContext context) {

    if (CollectionUtils.isEmpty(monitorList)) {
      return;
    }

    monitorList.stream().forEach(monitor -> {

      if (monitor.support(context)) {

        monitor.handler(context);
      }
    });


  }


}
