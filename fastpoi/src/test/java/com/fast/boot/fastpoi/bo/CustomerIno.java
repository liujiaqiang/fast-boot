package com.fast.boot.fastpoi.bo;

import com.fast.boot.fastpoi.annotation.ExcelField;

import lombok.Data;

/**
 * @author: junqing.li
 * @date: 18/1/14
 */
@Data
public class CustomerIno {

    /**
     * 客户姓名
     */
    @ExcelField(name = "客户姓名")
    private String customerName;

    /**
     * 身份证号
     */
    @ExcelField(name = "身份证号")
    private String idNo;

    /**
     * 手机号
     */
    @ExcelField(name = "手机号")
    private String phone;

    /**
     * 客户ID
     */
    @ExcelField(name = "客户ID")
    private Long customerId;

    /**
     * 期数
     */
    @ExcelField(name = "期数")
    private int terms;

    /**
     * 信用额度（已废弃）
     */
    @ExcelField(name = "授信额度")
    private Double creditAmount;
}
