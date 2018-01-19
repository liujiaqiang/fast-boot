package com.fast.boot.spring.cloud.ext;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * 基于服务发现metadata的路由规则
 *
 * @author: junqing.li
 * @date: 17/10/19
 */
@Slf4j
public class DiscoveryMetadataRule extends ZoneAvoidanceRule {


  /**
   * 选择服务
   * 
   * @param key
   * @return
   */
  @Override
  public Server choose(Object key) {


    // 如何获得网关处下发的数据
    log.info("[choose] choose userId={}");

    ILoadBalancer lb = getLoadBalancer();

    List<Server> serverList = this.getPredicate().getEligibleServers(lb.getAllServers(), key);
    if (CollectionUtils.isEmpty(serverList)) {
      return null;
    }

    // 一个应用部署的所有服务
    for (Server server : serverList) {
      DiscoveryEnabledServer discoveryServer = (DiscoveryEnabledServer) server;

      // 获取一个实例信息
      InstanceInfo instanceInfo = discoveryServer.getInstanceInfo();
      Map<String, String> metadata = instanceInfo.getMetadata();



    }

    return super.choose(key);
  }
}
