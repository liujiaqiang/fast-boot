package com.fast.boot.fastpoi.annotation;

import static com.fast.boot.fastpoi.FieldInfo.DATETIME_FORMAT;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author: junqing.li
 * @date: 18/1/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {

    /**
     * 列名
     */
    String name() default "";

    /**
     * 格式（作用于日期）
     */
    String format() default DATETIME_FORMAT;

    /**
     * 宽度
     */
    int width() default 0;

    /**
     * 顺序
     */
    int order() default 0;

    /**
     * 为null时的默认值
     */
    String defaultValue() default "";

    /**
     * 分隔符 在合并字段时使用
     */
    String separator() default "";
}
