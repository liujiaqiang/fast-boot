package com.fast.boot.api;

import java.io.Serializable;

import lombok.Data;

/**
 * 分页参数
 * 
 * @author: junqing.li
 */
@Data
public class Pagination implements Serializable {

    private static final long serialVersionUID = 5069015456039159642L;

    /** 每页大小 */
    private Integer pageSize;

    /** 当前第几页 */
    private Integer pageNum;

    private static final int DEFAULT_PAGE_SIZE = 10;

    public Pagination() {
        this.pageNum = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public Pagination(int pageSize, int pageCurrent) {
        this.pageNum = pageCurrent < 1 ? 1 : pageCurrent;
        this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }
}
