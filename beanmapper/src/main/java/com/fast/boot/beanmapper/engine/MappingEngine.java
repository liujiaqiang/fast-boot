package com.fast.boot.beanmapper.engine;

import java.util.function.Function;

import com.fast.boot.beanmapper.BeanConverter;

/**
 * Created by junqing.li on 17/2/28.
 */
public interface MappingEngine {

  /**
   * 直接转换
   *
   * @param source
   * @param target
   * @param <S>
   * @param <T>
   * @return
   */
  <S, T> T map(S source, Class<T> target);

  /**
   * 默认相同属性转换 后处理
   *
   * @param source
   * @param target
   * @param convert
   * @param <S>
   * @param <T>
   * @return
   */
  <S, T> T map(S source, Class<T> target, BeanConverter<S, T> convert);

  /**
   * 
   * @param source
   * @param target
   * @param function
   * @param <S>
   * @param <T>
   * @return
   */
  <S, T> T map(S source, Class<T> target, Function<S, T> function);
}
