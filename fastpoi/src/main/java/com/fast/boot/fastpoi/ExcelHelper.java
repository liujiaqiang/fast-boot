package com.fast.boot.fastpoi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * excel导入导出支持
 * 
 * @author: junqing.li
 * @date: 18/1/13
 */
public class ExcelHelper {

    /**
     * 读取流 输出map类型[看用于excel转换直接入库json数据]
     * 
     * @param in
     * @return
     */
    public static List<Map<String, String>> read(InputStream in) {

        return ReadUtils.read(in);
    }

    /**
     * 读取流 输出类
     * 
     * @param in
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> read(InputStream in, Class<T> tClass) {

        return ReadUtils.read(in, tClass);
    }

    /**
     * 写入流 list的第一位置 是头部
     * 
     * @param list
     * @return
     */
    public static byte[] write(List<Map<String, String>> list) {
        return WriteUtils.write(list).toNativeBytes();
    }

    /**
     * 写入流 list的第一位置 是头部
     *
     * @param collection
     * @return
     */
    public static <T> byte[] write(Collection<T> collection, Class<T> tClass) {
        try {
            return WriteUtils.write(collection, tClass).toNativeBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 到处到浏览器
     * 
     * @param list
     * @param fileName
     * @param response
     */
    public static void export(List<Map<String, String>> list, String fileName, HttpServletResponse response) {

        try {

            PoiUtils.writeBrowser(fileName, WriteUtils.write(list).toNativeWorkbook(), response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 到处list
     * 
     * @param collection 要到处的数据
     * @param tClass 到处的类
     * @param fileName 文件名称
     * @param response
     * @param <T>
     */
    public static <T> void export(Collection<T> collection, Class<T> tClass, String fileName,
        HttpServletResponse response) {

        try {

            PoiUtils.writeBrowser(fileName, WriteUtils.write(collection, tClass).toNativeWorkbook(), response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
