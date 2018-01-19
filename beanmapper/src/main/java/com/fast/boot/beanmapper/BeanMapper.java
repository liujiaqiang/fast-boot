package com.fast.boot.beanmapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fast.boot.beanmapper.engine.MappingEngine;
import com.fast.boot.beanmapper.engine.SpringMappingEngineImpl;


/**
 * Created by junqing.li on 17/2/28.
 */
public class BeanMapper {

  private final static MappingEngine mappingEngine = new SpringMappingEngineImpl();

  /**
   * 默认转换
   *
   * @param source
   * @param target
   * @param <T>
   * @return
   */
  public static <S, T> T map(S source, Class<T> target) {

    return mappingEngine.map(source, target);
  }

  /**
   * 转换 自己在function中new 目标对象 操作后自动复制
   * 
   * @param source
   * @param target
   * @param function
   * @param <S>
   * @param <T>
   * @return
   */
  public static <S, T> T map(S source, Class<T> target, Function<S, T> function) {

    return mappingEngine.map(source, target, function);
  }

  /**
   * 转换，不用new目标对象
   *
   * @param source
   * @param target
   * @param converter
   * @param <S>
   * @param <T>
   * @return
   */
  public static <S, T> T map(S source, Class<T> target, BeanConverter<S, T> converter) {

    return mappingEngine.map(source, target, converter);
  }

  /**
   * 默认直接 转换
   *
   * @param collection
   * @param target
   * @param <S>
   * @param <T>
   * @return
   */
  public static <S, T> List<T> mapList(Collection<S> collection, Class<T> target) {

    if (Objects.isNull(collection)) {
      return Collections.emptyList();
    }

    return collection.stream().map(source -> map(source, target)).collect(Collectors.toList());
  }

  /**
   * @param collection
   * @param target
   * @param function
   * @param <S>
   * @param <T>
   * @return
   */
  public static <S, T> List<T> mapList(Collection<S> collection, Class<T> target,
      Function<S, T> function) {

    if (Objects.isNull(collection)) {
      return Collections.emptyList();
    }

    return collection.stream().map(source -> map(source, target, function))
        .collect(Collectors.toList());
  }

  /**
   * list转换
   *
   * @param target
   * @param convert
   * @param <S>
   * @param <T>
   * @return
   */
  public static <S, T> List<T> mapList(Collection<S> collection, Class<T> target,
      BeanConverter<S, T> convert) {

    if (Objects.isNull(collection)) {
      return Collections.emptyList();
    }

    return collection.stream().map(source -> map(source, target, convert))
        .collect(Collectors.toList());
  }

  /**
   * 并发map 不用手动生成对象
   *
   * @param collection
   * @param target
   * @param convert
   * @param <S>
   * @param <T>
   * @return
   */
  public static <S, T> List<T> parallelMap(Collection<S> collection, Class<T> target,
      BeanConverter<S, T> convert) {

    if (Objects.isNull(collection)) {
      return Collections.emptyList();
    }

    return collection.parallelStream().map(source -> map(source, target, convert))
        .collect(Collectors.toList());
  }

  /**
   * 自己生成对象 然后自动copy
   *
   * @param collection
   * @param target
   * @param function
   * @param <S>
   * @param <T>
   * @return
   */
  public static <S, T> List<T> parallelMap(Collection<S> collection, Class<T> target,
      Function<S, T> function) {

    if (Objects.isNull(collection)) {
      return Collections.emptyList();
    }

    return collection.parallelStream().map(source -> map(source, target, function))
        .collect(Collectors.toList());
  }
}
