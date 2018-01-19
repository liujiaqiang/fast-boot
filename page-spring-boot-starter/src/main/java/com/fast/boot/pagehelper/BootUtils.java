package com.fast.boot.pagehelper;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.fast.boot.api.BootResult;
import com.fast.boot.api.Page;
import com.fast.boot.api.Pagination;
import com.fast.boot.beanmapper.BeanMapper;

/**
 * Created by junqing.li on
 * 17/4/17. 返回结果封装
 */
public class BootUtils {

  /**
   * 成功
   *
   * @param data
   * @param <T>
   * @return
   */
  public static <T> BootResult<T> success(T data) {

    return BootResult.success(data);
  }


  /**
   * 失败
   *
   * @param
   * @return
   */
  public static BootResult fail(String code, String msg) {

    return BootResult.fail(code, msg);
  }


  /**
   * 分页返回
   *
   * @param list
   * @param clazz
   * @param function
   * @param <T>
   * @param <S>
   * @return
   */
  public static <T, S> Page<T> page(List<S> list, Class<T> clazz, Function<S, T> function) {
    return toPage(list, clazz, function, false);
  }


  /**
   * 默认直接转换
   *
   * @param list
   * @param clazz
   * @param <T>
   * @param <S>
   * @return
   */
  public static <T, S> Page<T> page(List<S> list, Class<T> clazz) {

    return page(list, clazz, source -> BeanMapper.map(source, clazz));
  }

  /**
   * 同一个类型
   *
   * @param list
   * @param <T>
   * @return
   */
  public static <T> Page<T> page(List<T> list) {

    com.github.pagehelper.Page<T> page = (com.github.pagehelper.Page<T>) list;

    Page<T> result = new Page();
    result.setList(list);
    result.setPageNum(page.getPageNum());
    result.setPageSize(page.getPageSize());
    result.setTotalPages(page.getPages());
    result.setTotalCount(page.getTotal());

    return result;
  }

  /**
   * 返回一个空的page
   *
   * @param <T>
   * @return
   */
  public static <T> Page<T> EmptyPage(Pagination pagination) {
    Page<T> result = new Page();
    result.setList(new ArrayList<T>());
    result.setPageSize(pagination.getPageSize());
    result.setPageNum(pagination.getPageNum());
    result.setTotalPages(0);
    result.setTotalCount(0);
    return result;
  }

  /**
   * 并发执行
   *
   * @param list
   * @param clazz
   * @param function
   * @param <T>
   * @param <S>
   * @return
   */
  public static <T, S> Page<T> pageParallel(List<S> list, Class<T> clazz, Function<S, T> function) {
    return toPage(list, clazz, function, true);
  }


  private static <T, S> Page<T> toPage(List<S> list, Class<T> clazz, Function<S, T> function,
      boolean isParallet) {
    com.github.pagehelper.Page<S> page = (com.github.pagehelper.Page<S>) list;

    Page<T> result = new Page();
    List<T> tList;
    if (isParallet) {
      tList = BeanMapper.parallelMap(page.getResult(), clazz, function);
    } else {
      tList = BeanMapper.mapList(page.getResult(), clazz, function);
    }
    result.setList(tList);
    result.setPageNum(page.getPageNum());
    result.setPageSize(page.getPageSize());
    result.setTotalPages(page.getPages());
    result.setTotalCount(page.getTotal());

    return result;
  }
}
