package com.fast.boot.beanmapper.engine;

import java.util.Objects;

import org.springframework.beans.BeanUtils;

/**
 * Created by junqing.li on 17/3/1.
 */
public class SpringMappingEngineImpl extends AbstractMappingEngine {

  @Override
  protected <T, S> T mapInternal(S source, T target) {

    if (Objects.isNull(source)) {
      return null;
    }

    BeanUtils.copyProperties(source, target);

    return target;
  }
}
