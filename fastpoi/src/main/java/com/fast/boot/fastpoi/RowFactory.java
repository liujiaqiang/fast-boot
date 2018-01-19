package com.fast.boot.fastpoi;

import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * 创建row
 * 
 * @author: junqing.li
 * @date: 18/1/13
 */
public final class RowFactory {

    private RowFactory() {

    }

    public static Row getOrCreate(Sheet sheet, int index) {

        Validate.notNull(sheet);
        Row row = sheet.getRow(index);
        if (Objects.isNull(row)) {
            return sheet.createRow(index);
        }
        return row;
    }
}
