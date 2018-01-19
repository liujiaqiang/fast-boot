package com.fast.boot.fastpoi;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.collect.Lists;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import com.fast.boot.fastpoi.annotation.ExcelField;

/**
 * @author: junqing.li
 * @date: 18/1/14
 */
class WriteUtils {

    /**
     * 写入流
     * 
     * @param list
     * @return
     */
    public static WorkbookContext write(List<Map<String, String>> list) {

        Validate.notEmpty(list, "输入数据不能为空");

        // 取得excel表头
        List<String> headerList = Lists.newArrayList(list.get(0).keySet());

        // 创建workbook上下文
        WorkbookContext workbookContext = WorkbookFactory.createWorkbook(WorkbookType.SXSSF);

        // 创建表头
        SheetContext sheetContext
            = workbookContext.createSheetAndHeader(headerList.toArray(new String[headerList.size()]));

        for (Map<String, String> clomun : list) {

            RowContext rowContext = sheetContext.nextRow();

            for (String clomunName : clomun.keySet()) {
                rowContext.text(clomun.get(clomunName));
            }
        }
        return workbookContext;
    }

    public static <T> WorkbookContext write(Collection<T> collection, Class<T> tClass)

        throws Exception {

        Validate.notEmpty(collection, "数据不能为空");

        WorkbookContext workbookContext = WorkbookFactory.createWorkbook(WorkbookType.SXSSF);
        List<FieldInfo> fieldInfoList = getFieldInfoList(tClass);

        List<String> headerList
            = fieldInfoList.stream().map(fieldInfo -> fieldInfo.getName()).collect(Collectors.toList());
        // 创建表头
        SheetContext sheetContext
            = workbookContext.createSheetAndHeader(headerList.toArray(new String[headerList.size()]));

        // 根据不同类型写row和cell
        for (T t : collection) {
            doRow(sheetContext, fieldInfoList, t);
        }
        return workbookContext;
    }

    private static <T> List<FieldInfo> getFieldInfoList(Class<T> tClass) throws IntrospectionException {

        List<Field> fieldList = FieldUtils.getFieldsListWithAnnotation(tClass, ExcelField.class);
        List<FieldInfo> result = Lists.newArrayList();
        for (Field field : fieldList) {
            FieldInfo fieldInfo = new FieldInfo();
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            fieldInfo.setField(field);
            fieldInfo.setOrder(excelField.order());
            fieldInfo.setWidth(excelField.width());
            fieldInfo.setFormat(excelField.format());
            fieldInfo.setName(StringUtils.isBlank(excelField.name()) ? field.getName() : excelField.name());
            // 内省得到get信息
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), tClass);
            Method getMethod = pd.getReadMethod();
            fieldInfo.setMethod(getMethod);
            result.add(fieldInfo);
        }

        return result;
    }

    private static void doRow(SheetContext sheetContext, List<FieldInfo> fieldInfoList, Object object)
        throws InvocationTargetException, IllegalAccessException {

        RowContext rowContext = sheetContext.nextRow();
        for (FieldInfo fieldInfo : fieldInfoList) {

            Object value = fieldInfo.getMethod().invoke(object);
            TypeToken typeToken = Invokable.from(fieldInfo.getMethod()).getReturnType();
            setRowValue(rowContext, typeToken, value, fieldInfo);

            if (fieldInfo.getWidth() > 0) {
                rowContext.setColumnWidth(fieldInfo.getWidth());
            }

        }
    }

    private static void setRowValue(RowContext rowContext, TypeToken typeToken, Object value, FieldInfo fieldInfo) {

        Class typeClass
            = typeToken.isPrimitive() ? ClassUtils.primitiveToWrapper(typeToken.getRawType()) : typeToken.getRawType();

        if (String.class.isAssignableFrom(typeClass)) {
            rowContext.text((String)value);
            return;
        }

        if (Number.class.isAssignableFrom(typeClass)) {
            rowContext.number((Number)value);
            return;
        }

        if (typeClass.isAssignableFrom(BigDecimal.class)) {
            rowContext.text(String.valueOf(value));
            return;
        }
        if (typeClass.isAssignableFrom(Boolean.class)) {
            rowContext.text(String.valueOf(value));
            return;
        }

        if (typeClass.isAssignableFrom(Date.class)) {
            rowContext.date((Date)value, fieldInfo.getFormat());
            return;
        }

        String text = Objects.isNull(value) ? StringUtils.EMPTY : StringUtils.trimToEmpty(String.valueOf(value));

        rowContext.text(text);

    }
}
