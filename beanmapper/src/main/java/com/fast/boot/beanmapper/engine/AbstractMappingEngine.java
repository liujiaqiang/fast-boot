package com.fast.boot.beanmapper.engine;

import java.util.function.Function;

import org.springframework.beans.BeanUtils;

import com.fast.boot.beanmapper.BeanConverter;



/**
 * Created by junqing.li on 17/3/1. 转换父类
 */
public abstract class AbstractMappingEngine implements MappingEngine {

  @Override
  public <S, T> T map(S source, Class<T> target) {

    T targetInstance = BeanUtils.instantiate(target);

    return mapInternal(source, targetInstance);
  }

  protected abstract <T, S> T mapInternal(S source, T target);

  @Override
  public <S, T> T map(S source, Class<T> target, BeanConverter<S, T> convert) {

    T instance = map(source, target);

    convert.convert(source, instance);

    return instance;
  }

  /**
   *
   * @param source
   * @param target
   * @param function
   * @param <S>
   * @param <T>
   * @return
   */
  @Override
  public <S, T> T map(S source, Class<T> target, Function<S, T> function) {

    T instance = function.apply(source);

    // mapInternal(source, instance); [bug]

    return instance;
  }
}
