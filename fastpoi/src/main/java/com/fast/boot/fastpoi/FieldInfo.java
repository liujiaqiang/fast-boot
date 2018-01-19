package com.fast.boot.fastpoi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.Data;

/**
 * 属性信息
 * 
 * @author: junqing.li
 * @date: 18/1/14
 */
@Data
public class FieldInfo {

    public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 表头
     */
    private String name;

    /**
     * 顺序
     */
    private int order;

    /**
     * 格式
     */
    private String format = DATETIME_FORMAT;

    /**
     * 宽度
     */
    private int width;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 标记
     */
    private int[] tags;

    /**
     * 合并到
     */
    private String mergeTo;

    /**
     * 分隔符
     */
    private String separator;

    /**
     * 对应的属性
     */
    private Field field;

    private Method method;
}
