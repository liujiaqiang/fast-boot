package com.fast.boot.api;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by junqing.li on 17/4/17. 项目统一page对象
 */
@Data
public class Page<T> implements Serializable {

  private static final long serialVersionUID = -6630108958468043158L;

  /** 当前页号，从1开始 */
  private int pageNum = 1;

  /** 每页数量 */
  private int pageSize = 10;

  /** 总页数 */
  private int totalPages = 1;

  /** 总记录数 */
  private long totalCount = 0;

  /** 数据集合 */
  private List<T> list;
}
