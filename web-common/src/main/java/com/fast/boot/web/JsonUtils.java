package com.fast.boot.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletResponse;

import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.google.common.base.Charsets;
import com.google.common.io.Closer;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by junqing.li on 17/4/18.
 */
@Slf4j
public class JsonUtils {

  /**
   * fastjson配置
   *
   * 项目通用格式 web对外通用
   *
   * 【别动】
   * 
   * @return
   */
  public static FastJsonConfig fastJsonConfig() {

    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setCharset(Charsets.UTF_8);
    fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
    fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteDateUseDateFormat,
        SerializerFeature.WriteMapNullValue);
    fastJsonConfig.setSerializeFilters(new LongValueFilter());
    return fastJsonConfig;
  }

  /**
   * to joson
   * 
   * @param obj
   * @return
   */
  public static String toJSON(Object obj) {

    FastJsonConfig fastJsonConfig = fastJsonConfig();

    return JSON.toJSONString(obj, fastJsonConfig.getSerializeConfig(),
        fastJsonConfig.getSerializeFilters(), fastJsonConfig.getDateFormat(),
        JSON.DEFAULT_GENERATE_FEATURE, fastJsonConfig.getSerializerFeatures());

  }

  /**
   * to joson
   *
   * @param obj
   * @return
   */
  public static String toJSON(Object obj, FastJsonConfig fastJsonConfig) {

    return JSON.toJSONString(obj, fastJsonConfig.getSerializeConfig(),
        fastJsonConfig.getSerializeFilters(), fastJsonConfig.getDateFormat(),
        JSON.DEFAULT_GENERATE_FEATURE, fastJsonConfig.getSerializerFeatures());

  }


  /**
   * 写obj
   * 
   * @param response
   * @param obj
   * @throws IOException
   */
  public static void writer(ServletResponse response, Object obj) {

    response.setCharacterEncoding(Charsets.UTF_8.name());
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

    Closer closer = Closer.create();
    try {
      OutputStream writer = response.getOutputStream();
      closer.register(writer);

      FastJsonConfig fastJsonConfig = fastJsonConfig();
      JSON.writeJSONString(writer, fastJsonConfig.getCharset(), obj,
          fastJsonConfig.getSerializeConfig(), fastJsonConfig.getSerializeFilters(),
          fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE,
          fastJsonConfig.getSerializerFeatures());

    } catch (IOException e) {
      log.error("[writer] exception=", e);

    } finally {
      try {
        closer.close();
      } catch (IOException e) {
        log.error("[writer] exception=", e);
      }
    }
  }

  /**
   * 处理 Long在js的json解析问题
   *
   * https://github.com/alibaba/fastjson/issues/207
   */
  public static class LongValueFilter implements ValueFilter {

    @Override
    public Object process(Object object, String name, Object value) {

      if (value instanceof Long) {

        String valueStr = String.valueOf(value);
        if (valueStr.length() >= 18) {
          return valueStr;
        }
      }
      return value;
    }
  }



}
