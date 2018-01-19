package com.fast.boot.api;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by junqing.li on 17/4/14
 *
 * rpc和http通用返回对象
 */
@Data
public class BootResult<T> implements Serializable {

  private static final long serialVersionUID = -1339496790543320624L;

  /** code码 */
  private String code;

  /** 对于返回信息 */
  private String msg;

  private T result;

  private BootResult() {

  }

  /**
   * 判断是否成功
   * 
   * @return
   */
  public boolean isSuccess() {

    return this.code.equals(BootCode.SUCCESS);
  }

  /**
   * 成功方法
   *
   * @param data
   * @param
   * @return
   */
  public static BootResult success(Object data) {
    BootResult result = new BootResult();
    result.setCode(BootCode.SUCCESS);
    result.setResult(data);
    result.setMsg("SUCCESS");
    return result;
  }

  /**
   * 成功方法
   *
   * @param
   * @return
   */
  public static BootResult success() {

    BootResult result = new BootResult();
    result.setCode(BootCode.SUCCESS);
    result.setMsg("SUCCESS");
    return result;
  }


  public static BootResult fail(String code, String msg) {

    BootResult result = new BootResult();
    result.setCode(code);
    result.setMsg(msg);
    return result;
  }

}
