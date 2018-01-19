package com.fast.boot.fastpoi;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.io.Closer;

/**
 * @author: junqing.li
 * @date: 17/6/28
 */
public class PoiUtils {

    private static final String XLSX_SUFFIX = ".xlsx";

    private static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * 写excel到浏览器
     * 
     * @param response
     * @param fileName
     * @param wb
     * @throws Exception
     */
    public static void writeBrowser(String fileName, Workbook wb, HttpServletResponse response) throws IOException {

        Closer closer = Closer.create();

        response.setContentType(XLSX_CONTENT_TYPE);
        response.setHeader("Content-disposition",
            "attachment; filename=" + URLEncoder.encode(String.format("%s%s", fileName, XLSX_SUFFIX), "UTF-8"));
        OutputStream out = response.getOutputStream();
        wb.write(out);
        closer.register(out);
        out.flush();

        closer.close();
    }
}
