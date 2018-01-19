package com.fast.boot.fastpoi;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import com.fast.boot.fastpoi.annotation.ExcelField;

/**
 * 内部类
 * 
 * @author: junqing.li
 * @date: 18/1/13
 */
class ReadUtils {

    /**
     * 读取流 输出map类型[看用于excel转换直接入库json数据]
     *
     * @param in
     * @return
     */
    protected static List<Map<String, String>> read(InputStream in) {

        try {

            Sheet sheet = createSheet(in);
            if (Objects.isNull(sheet)) {
                return Collections.emptyList();
            }

            Iterator<Row> rows = sheet.rowIterator();
            if (Objects.isNull(rows)) {
                return Collections.emptyList();
            }

            boolean firstRow = true;
            List<String> titleList = Lists.newArrayList();
            List<Map<String, String>> result = Lists.newArrayList();
            while (rows.hasNext()) {
                Row row = rows.next();
                if (isEnd(row)) {
                    break;
                }
                Map<String, String> map = new LinkedHashMap<>();
                int rowSize = firstRow ? row.getLastCellNum() : titleList.size();
                for (int i = row.getFirstCellNum(); i < rowSize; i++) {
                    Cell cell = row.getCell(i);
                    // 取得cell值
                    String cellValue = Strings.nullToEmpty(getCellFormatValue(cell));
                    if (firstRow && !Strings.isNullOrEmpty(cellValue)) {
                        titleList.add(cellValue);
                        continue;
                    }
                    if (i < rowSize) {
                        String key = titleList.get(i);
                        map.put(key, cellValue);
                    }
                }
                // 每行记录加入
                if (map.size() > 0) {
                    result.add(map);
                }
                // 第一行设置为否
                firstRow = false;

            }

            return result;

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    protected static <T> List<T> read(InputStream in, Class<T> tClass) {

        try {

            Sheet sheet = createSheet(in);
            if (Objects.isNull(sheet)) {
                return Collections.emptyList();
            }
            Iterator<Row> rows = sheet.rowIterator();
            if (Objects.isNull(rows)) {
                return Collections.emptyList();
            }

            // 第一行为标题
            Iterator<Row> rowIterator = sheet.rowIterator();
            List<String> titleList = getTitleList(rowIterator);
            Map<String, FieldInfo> fieldInfoMap = getFileInfoMap(tClass);
            List<T> result = new ArrayList<>();

            // 可以加个缓存
            T object = tClass.newInstance();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (isEnd(row)) {
                    break;
                }
                for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i);
                    if (Objects.isNull(cell)) {
                        continue;
                    }
                    String titleString = titleList.get(i);
                    reflectSetValue(object, cell, fieldInfoMap, titleString);
                }
                result.add(object);
            }
            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 反射 赋值
     * 
     * @param object
     * @param cell
     * @param fieldInfoMap
     * @param titleString
     * @param <T>
     */
    private static <T> void reflectSetValue(T object, Cell cell, Map<String, FieldInfo> fieldInfoMap,

        String titleString) throws ParseException, InvocationTargetException, IllegalAccessException {

        FieldInfo fieldInfo = fieldInfoMap.get(titleString);
        if (Objects.isNull(fieldInfo)) {
            return;
        }
        TypeToken typeToken = Invokable.from(fieldInfo.getMethod()).getParameters().get(0).getType();

        fieldInfo.getMethod().invoke(object, getValueByCell(typeToken, cell, fieldInfo));

    }

    private static Object getValueByCell(TypeToken typeToken, Cell cell, FieldInfo fieldInfo) throws ParseException {

        // 转包装类型
        Class typeClass
            = typeToken.isPrimitive() ? ClassUtils.primitiveToWrapper(typeToken.getRawType()) : typeToken.getRawType();

        if (typeClass.isAssignableFrom(String.class)) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        }

        // 日期
        if (typeClass.isAssignableFrom(Date.class)) {
            // 日期格式
            if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                return cell.getDateCellValue();
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            return DateUtils.parseDate(cell.getStringCellValue(), fieldInfo.getFormat());
        }

        if (typeClass.isAssignableFrom(Boolean.class)) {
            if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
                return cell.getBooleanCellValue();
            }
            return cell.getStringCellValue().equalsIgnoreCase("true") || (!cell.getStringCellValue().equals("0"));
        }

        if (typeClass.isAssignableFrom(Integer.class)) {

            if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                return Double.valueOf(cell.getNumericCellValue()).intValue();
            }
            return Integer.valueOf(cell.getStringCellValue());
        }

        if (typeClass.isAssignableFrom(Long.class)) {

            if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                return Double.valueOf(cell.getNumericCellValue()).longValue();
            }
            return Long.valueOf(cell.getStringCellValue());
        }

        if (typeClass.isAssignableFrom(Double.class)) {

            if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                return cell.getNumericCellValue();
            }
            return Double.valueOf(cell.getStringCellValue());
        }

        if (typeClass.isAssignableFrom(BigDecimal.class)) {

            if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                return new BigDecimal(cell.getNumericCellValue());
            }
            return new BigDecimal(cell.getStringCellValue());
        }

        cell.setCellType(Cell.CELL_TYPE_STRING);
        String cellValue = cell.getStringCellValue();
        return StringUtils.trimToEmpty(cellValue);

    }

    private static List<String> getTitleList(Iterator<Row> rowIterator) {

        Row titleRow = rowIterator.next();
        // 标题cell
        Iterator<Cell> cellTitleIterator = titleRow.cellIterator();

        List<String> titleList = new ArrayList<>();
        while (cellTitleIterator.hasNext()) {
            Cell cell = cellTitleIterator.next();
            String cellValue = cell.getStringCellValue();
            String title = StringUtils.isBlank(cellValue) ? "" : cellValue;
            titleList.add(title);
        }

        return titleList;

    }

    private static Map<String, FieldInfo> getFileInfoMap(Class<?> clazz) throws IntrospectionException {

        List<Field> fieldList = FieldUtils.getFieldsListWithAnnotation(clazz, ExcelField.class);
        Map<String, FieldInfo> map = new HashMap<>();
        for (Field field : fieldList) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            FieldInfo fieldInfo = toFiledInfo(field, excelField, clazz);
            map.put(fieldInfo.getName(), fieldInfo);
        }
        return map;
    }

    /**
     * 转换对象
     * 
     * @param field
     * @param excelField
     * @return
     */
    private static <T> FieldInfo toFiledInfo(Field field, ExcelField excelField, Class<T> clazz)
        throws IntrospectionException {

        FieldInfo fieldInfo = new FieldInfo();

        fieldInfo.setName(excelField.name());
        fieldInfo.setOrder(excelField.order());
        fieldInfo.setWidth(excelField.width());
        fieldInfo.setDefaultValue(excelField.defaultValue());
        fieldInfo.setField(field);
        fieldInfo.setFormat(excelField.format());

        // 扫描属性
        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
        Method method = pd.getWriteMethod();

        fieldInfo.setMethod(method);
        return fieldInfo;
    }

    private static Sheet createSheet(InputStream in) throws IOException, InvalidFormatException {

        Workbook book = null;
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            book = new HSSFWorkbook(in);
        } else if (POIXMLDocument.hasOOXMLHeader(in)) {
            book = new XSSFWorkbook(OPCPackage.open(in));
        }
        if (Objects.isNull(book)) {
            return null;
        }
        // 默认取得第一个sheet
        return book.getSheetAt(0);
    }

    /**
     * 空行作为结束
     * 
     * @param row
     */
    private static boolean isEnd(Row row) {

        if (Objects.isNull(row)) {
            return true;
        }
        boolean result = true;
        Iterator<Cell> cells = row.cellIterator();
        String value = "";
        while (cells.hasNext()) {
            Cell cell = cells.next();
            int cellType = cell.getCellType();
            switch (cellType) {
                case Cell.CELL_TYPE_NUMERIC:
                    value = String.valueOf(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    value = String.valueOf(cell.getCellFormula());
                    break;
            }
            if (StringUtils.isNotBlank(value)) {
                result = false;
                break;
            }
        }

        return result;
    }

    /**
     * 获取 cell的值
     * 
     * @param cell
     * @return
     */
    private static String getCellFormatValue(Cell cell) {

        if (Objects.isNull(cell)) {
            return "";
        }
        String cellValue = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
            case Cell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    Date date = cell.getDateCellValue();
                    cellValue = DateFormatUtils.format(date, FieldInfo.DATETIME_FORMAT);
                } else {
                    // 如果是纯数字
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRING
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            default:
                cellValue = "";
        }

        return cellValue;
    }

}
