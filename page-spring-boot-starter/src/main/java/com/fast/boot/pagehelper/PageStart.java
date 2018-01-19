package com.fast.boot.pagehelper;

import java.util.Objects;

import com.github.pagehelper.PageHelper;
import com.fast.boot.api.Pagination;

/**
 * Created by junqing.li on 17/4/17.
 */
public abstract class PageStart {

  /**
   * 开始分页
   * 
   * @param pagination
   */
  public static void now(Pagination pagination) {

    pagination = Objects.isNull(pagination) || Objects.isNull(pagination.getPageNum())
        ? new Pagination() : pagination;

    PageHelper.startPage(pagination.getPageNum(), pagination.getPageSize());

  }

}
