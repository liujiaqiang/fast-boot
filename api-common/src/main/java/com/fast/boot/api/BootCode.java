package com.fast.boot.api;

/**
 *
 * 错误码标记
 *
 * @author: junqing.li
 * @date: 17/7/18
 */
public abstract class BootCode {

    /** 成功 */
    public static final String SUCCESS = "G_0000";

    /** 权限验证失败 */
    public static final String PERMISSION_ERROR = "G_0006";

    /** 登录验证失败 */
    public static final String AUTH_ERROR = "G_0007";

    /** 参数失败 */
    public static final String ARGUMENT_ERROR = "G_0008";

    /** sql异常 */
    public static final String SQL_ERROR = "G_0009";

    /** 业务异常 */
    public static final String BIZ_ERROR = "G_6666";

    /** 内部dubbo服务调用异常 */
    public static final String DUBBO_ERROR = "G_8888";

    /** HTTP服务调用异常 */
    public static final String HTTP_ERROR = "G_8889";

    /** 系统异常 */
    public static final String SYS_ERROR = "G_9999";

}
