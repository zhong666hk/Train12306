# Train12306
12306项目初始化  
## 添加common模块 和 注释注解
* 1.构建pom父工程  
**< build >** 标签删掉，不需要用maven打包
* 2.依赖的统一管理 **< dependencyManagement >**
* 3. springboot集成的http测试
```http request
GET http://localhost:8080/member/hello
Accept: application/json
```
### 构建common模块来存放公共的依赖和日志注解
* 日志注解  
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
    </dependencies>
```
```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAnnotation {
}
```  
**AOP增强**
```java
@Aspect
@Component
public class LogAspect {
    public static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Pointcut("@annotation(com.wbu.train.common.Aspect.annotation.LogAnnotation)")
    public void pointcut(){}

    @Around("pointcut()")
    public Object AroundAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        // 设置日志的自定义参数--流水号
        MDC.put("LOG_ID",System.currentTimeMillis()+ RandomUtil.randomString(3));
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        logger.info("方法名为{}",signature.getName());
        logger.info("方法参数类型为{}",signature.getParameterTypes());
        logger.info("方法参数{}",signature.getParameterNames());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        logger.info("方法返回值为{}",result);
        logger.info("方法执行耗时{}",end-start);
        return result;
    }
}
```
