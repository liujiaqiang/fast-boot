# fast-boot 项目基础组件封装

## aip相关common
* 定义通用返回异常
* 定义通用page
* 定义通用异常


## web相关common
* 定义controller处理模式
* 定义通用异常处理模式
* 集成swagger-ui


## 使用
* api-commmon：单独使用
* web-commmon: 基于spring-boot-web

```
/**
 *  需要继承下 AbstractMvcConfig
 */
@Configuration
public class WebConfig extends AbstractMvcConfig {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    super.addResourceHandlers(registry);
  }
}
```

* 使用之后统一了返回模型

* 在controller层返回 必须实现 提供的Controller 接口

```
@RestController
@RequestMapping("/sys")
public class GrayController implements Controller {

  @Autowired
  private GrayService grayService;

  @GetMapping("/getServerInfo")
  public List<GrayConfig> getServerInfo() {
    return grayService.getAllConfig();
  }
```

* 返回结果自动包装

```
{
    "code": "G_0000", 
    "msg": "SUCCESS", 
    "result": [
        {
            "app": "tsms", 
            "graying": true, 
            "serverIdList": [
                "localhost:8080", 
                "localhost:8585"
            ], 
            "toServerId": "localhost:8585", 
            "userList": null
        }
    ], 
    "success": true
}

```

## 统一异常处理

```

/**
 * web层异常详细处理
 * 预留扩展
 */
@Slf4j
@RestControllerAdvice
public class WebExceptionAdvice extends AbstractExceptionAdvice {


}

```
* 自定义异常记录后处理 比如报警灯

```

@Slf4j
@Component
public class WebExceptionHandler extends DefaultWebExceptionHandler {

  @Override
  protected void beforeLog(Exception e, HttpServletRequest request, Level level) {
    super.beforeLog(e, request, level);
  }

  @Override
  protected void afterLog(Exception e, HttpServletRequest request, Level level) {

    // 报警功能
    if (level.equals(Level.ERROR)) {

      log.error("[afterLog] 我正常报警 ");
    }
  }
}

```

## start user swagger ui
* in dev test start swagger in application.yml

```



spring:
    swagger:
       enable: true
       info:
         basePackage: com.fast.boot.gateway.web
         title: 网关API
         contact: junqing.li
         termsOfServiceUrl: http://xxx.api.com
         description: 网关提供入口
         version: 1.0

```

## 添加日志debug功能

```
spring:
    web:
      debug: true
      
```

## 监控模块使用
* 引入

```

```

### 例子
* 配置

```

# 配置应用的名称
spring:
  application:
        name: order-test
  mail:
    host: smtp.exmail.qq.com
    protocol: smtp
    password: 111111
    username: xxx@163.com
    properties:
            mail:
                smtp:
                  auth: true
                  timeout: 25000
                  connectiontimeout: 60000
                  writetimeout: 60000
            from: yymer@163.com
            # 发件人逗号分隔
            to: junqing.li@163.com, xxxx@163.com
            # 一分钟之内统一uri异常发送两次
            rate: 2 


```

### 使用

```

  @Autowired
  private MonitorManager monitorManager;

  @Autowired
  private Environment environment;

  @Override
  protected void beforeLog(Exception e, HttpServletRequest request, Level level) {
    super.beforeLog(e, request, level);
  }

  @Override
  protected void afterLog(Exception e, HttpServletRequest request, Level level) {

    try {

      // 报警功能
      if (level.equals(Level.ERROR)) {

        MonitorContext context = new MonitorContext();
        context.setException(ExceptionUtils.getStackTrace(e));
        context.setIp(WebUtils.getIpAddress(request));
        context.setParams(JsonUtils.toJSON(WebUtils.getParametersStartingWith(request, "")));
        context.setProfile(JsonUtils.toJSON(environment.getActiveProfiles()));
        context.setUri(request.getRequestURI());
        monitorManager.slack(context);

      }

    } catch (Exception ex) {

      log.error("[afterLog] ", ex);

    }
  }

```


##fastpoi
* excel导入导出

## spring-cloud 扩展
* 网关扩展
* 路由扩展
* 灰度处理



