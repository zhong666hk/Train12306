# Train12306
12306项目初始化  
## 0.02添加common模块 和 注释注解
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
## 0.03新增getway网关模块 
* 1配置路由转发  
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: member
          uri: http://127.0.0.1:8001
          predicates:
            - Path=/member/hello
        - id: member_reject
          uri: http://127.0.0.1:8001
          predicates:
            - Path=/member/**
```
* 2.配置gateway自带的日志  
在配置jvm参数中配置
```shell
-Dreactor.netty.http.server.accessLogEnabled=true
```
## 0.04 Mybatis-plus的整合统一返回结果、请求参数封装、全局异常封装
* **mybatis-plus的整合**
```yaml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3.1</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.26</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.28</version>
</dependency>
```
注意springBoot3.0和mybatis-plus的版本的整合  

全局异常和统一返回结果都是封装在**common模块中**  
请求封装都是放在各个模块当中  
*  **返回值的封装**
```java
@Data
public class CommonRespond <T>{
    //响应状态码
    private int code;
    // 响应信息
    private String message;

    // 返回结果
    private T data;

    public CommonRespond(){}

    public CommonRespond(int code,String message,T data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public CommonRespond(int code,String message){
        this(code,message,null);
    }

    public CommonRespond(int code,T data){
        this(code,null,data);
    }

    public static <T>CommonRespond<T> error(AppExceptionExample appExceptionExample){
        return new CommonRespond<>(appExceptionExample.getCode(), appExceptionExample.getMessage());
    }

    public static <T>CommonRespond<T> error(RespondExample respondExample){
        return new CommonRespond<>(respondExample.getCode(),respondExample.getMessage());
    }


    public static <T>CommonRespond<T> succeed(String message, T data){
        return new CommonRespond<>(200,message,data);
    }

    public static <T>CommonRespond<T> succeed(T data){
        return new CommonRespond<>(200,null,data);
    }
}
```
* **返回值的响应业务枚举**
```java
public enum RespondExample{
    INVALID_CODE(10000,"验证码无效"),
    USERNAME_NOT_EXISTS(10001,"用户名不存在"),
    USER_ROLE_NOT_PERMISSION(10003,"用户权限不足"),
    REQUEST_PARAMETER_IS_ILLEGAL(10004,"参数不合法");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    RespondExample(){}
    RespondExample(int code,String message){
        this.code=code;
        this.message=message;
    }
}
```
* **请求参数的封装**
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRegisterReq {
    private String mobile;
}
```

* **异常的封装**
```java
@Data
public class MyException extends RuntimeException{
    private int code;

    private String message;

    public MyException(){}

    public MyException(int code,String message){
        this.code=code;
        this.message=message;
    }
    public MyException(AppExceptionExample appExceptionExample){
        this(appExceptionExample.getCode(), appExceptionExample.getMessage());
    }
}
```
* **全局异常拦截器**
```java
@RestControllerAdvice
public class GlobalException {
    private final Logger logger = LoggerFactory.getLogger(GlobalException.class);
    /**
        第一个T表示<T>是一个泛型 --声明参数
        第二个T表示方法返回的是T类型的数据
    */
    @ExceptionHandler(MyException.class)
    public <T>CommonRespond<T> MyExceptionHandler(MyException e, ServletRequest request){
        logger.error(e.toString());
        return new CommonRespond<>(e.getCode(),e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public <T>CommonRespond<T> ExceptionHandler(Exception e, ServletRequest request){

        return CommonRespond.error(AppExceptionExample.SYSTEM_INNER_ERROR);
    }
}
```
* **异常枚举**
```java
public enum AppExceptionExample {
    SYSTEM_INNER_ERROR(500,"系统内部异常"),
    MEMBER_MOBILE_HAS_EXIST(10002,"该手机已注册过用户");
    private int code;
    private String message;

    private AppExceptionExample(int code, String message){
        this.code=code;
        this.message=message;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```
## 0.05 验证码接口、登录接口的开发
* 验证码接口
  * 创建验证码的数据库表CodeInformation和触发器-->过期时间是生成
  * 开发获取生成验证码的接口--(4位字符的)
  * ```mysql
    create table code_information
    (
    code_id         int auto_increment comment '主键'
    primary key,
    mobile          varchar(11)                        not null comment '手机号',
    code            varchar(10)                        not null comment '验证码',
    is_delete       tinyint  default 0                 null comment '是否删除 0未删除 1已经删除',
    create_time     datetime default CURRENT_TIMESTAMP null comment '创建时间',
    expiration_time timestamp                          null comment '过期时间',
    business_type   tinyint                            not null comment '业务类型',
    use_time        timestamp                          null on update CURRENT_TIMESTAMP comment '使用时间'
    )
    comment '验证码';
    create definer = root@localhost trigger set_expiration_time
    before insert
    on code_information
    for each row
    BEGIN
    SET NEW.expiration_time = NOW() + INTERVAL 30 MINUTE ;  -- 设置为当前时间加30分钟
    END;
``` ```
* 验证码接口的开发
```java
@LogAnnotation
    @PostMapping("/sendCode")
    public CommonRespond<String> sendCode(@Valid MemberSendCodeReq memberSendCodeReq) {
        if (ObjectUtil.isEmpty(memberSendCodeReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        return memberService.sendCode(memberSendCodeReq);
    }
```
* service的开发
```java
public CommonRespond<String> sendCode(MemberSendCodeReq memberRegisterReq) {
        //为空返回
        if (ObjectUtil.isEmpty(memberRegisterReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        String mobile = memberRegisterReq.getMobile();
        QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("mobile", mobile);
        List<Member> list = this.list(memberQueryWrapper);
        // 手机号不存在就创建并且发送验证码 存在就直接发送验证码
        if (CollectionUtil.isEmpty(list)) {
            Member member = new Member();
            member.setMobile(mobile);
            member.setId(SnowUtil.getSnowflakeNextId());
            // 注册失败就抛出异常
            if (!this.save(member)) {
                return new CommonRespond<>(500, "获取验证码失败");
            }
        }
        // 获取验证码
        String code = RandomUtil.randomString(4);
        // 保存短信记录表: 手机号，短信验证码，有效期，是否已经使用，业务类型，创建时间，使用时间
        CodeInformation codeInformation = new CodeInformation();
        codeInformation.setBusinessType(BusinessType.TYPE_LOGIN.getType());
        codeInformation.setCode(code);
        codeInformation.setMobile(mobile);
        if (!codeInformationService.save(codeInformation)){
            throw new MyException(10006,"codeInformationService插入异常");
        }
        //TODO 对接短信通道

        return CommonRespond.succeed(code);
    }
```
* 登录接口的开发
```java
@LogAnnotation
    @PostMapping("/login")
    public CommonRespond<LoginResp> login(@Valid MemberLoginReq memberLoginReq) {
        if (ObjectUtil.isEmpty(memberLoginReq)) {
            return CommonRespond.error(RespondExample.REQUEST_PARAMETER_IS_ILLEGAL);
        }
        return memberService.login(memberLoginReq);
    }
```
* 登录service的开发
```java
public CommonRespond<LoginResp> login(MemberLoginReq memberLoginReq) {
        // 根据这个信息去查codeInformation的消息 看验证码是否过期是否可以用
        String code = memberLoginReq.getCode();
        String mobile = memberLoginReq.getMobile();
        QueryWrapper<CodeInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",code).eq("mobile",mobile);
        CodeInformation codeInformation = codeInformationService.getOne(queryWrapper);
        //  未查到信息-->是逻辑删除  -->手机和验证码早就已经验证过了-->验证码已经使用过
        if (ObjectUtil.isNull(codeInformation)){
            throw new MyException(AppExceptionExample.MEMBER_CODE_HAS_USED);
        }
        // 如果验证码已经过期
        Date expirationTime = codeInformation.getExpirationTime();
        if (DateUtil.compare(expirationTime,new Date())<0){
            throw new MyException(AppExceptionExample.MEMBER_CODE_EXPIRE);
        }

        // 如果验证码类型不匹配
        if (!codeInformation.getBusinessType().equals(BusinessType.TYPE_LOGIN.getType())){
            throw new MyException(AppExceptionExample.MEMBER_CODE_TYPE_ERROR);
        }
        // 通过校验使用
        codeInformation.setUseTime(new Date());
        // -->更新使用时间
        if (!codeInformationService.updateById(codeInformation)) {
            throw new MyException(AppExceptionExample.SYSTEM_INNER_ERROR);
        }
        // 标记已经使用过
        if (!codeInformationService.removeById(codeInformation.getCodeId())) {
            throw new MyException(AppExceptionExample.SYSTEM_INNER_ERROR);
        }
        return CommonRespond.succeed("登陆成功",new LoginResp(true));
    }
```
* 登录响应的封装LoginResp
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResp {
    private boolean loginState;
}
```