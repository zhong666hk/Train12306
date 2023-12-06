package com.wbu.train.common.exception;

public enum AppExceptionExample {
    SYSTEM_INNER_ERROR(500,"系统内部异常"),
    MEMBER_MOBILE_HAS_EXIST(10002,"该手机已注册过用户"),
    MEMBER_MOBILE_OR_CODE_ERROR(10000,"验证码或手机号错误"),
    MEMBER_CODE_HAS_USED(10003,"验证码已经使用过"),

    MEMBER_CODE_EXPIRE(10001,"验证码已经过期"),
    MEMBER_CODE_TYPE_ERROR(10005,"请输入有效的验证码"),
    PASSENGER_SAVE_ERROR(10006,"乘客保存异常"),

    NOT_LOGIN(10007,"未登录"),
    PASSENGER_DELETE_ERROR(10008,"乘客删除异常"),
    STATION_SAVE_ERROR(10009,"站台保存/修改异常"),
    STATION_DELETE_ERROR(10010,"乘客删除异常"),
    ADMIN_HAS_EXIST(10011,"该管理员已经存在"),
    ADMIN_DELETE_ERROR(10012,"管理员删除异常"),
    ADMIN_NOT_EXIST(10012,"管理员不存在"),
    TRAIN_DELETE_ERROR(10013,"火车删除异常"),
    TRAIN_SEAT_GEN_ERROR(10014,"火车座位自动生成异常"),
    STATION_HAS_EXIST(10015,"站台已经存在"),
    TRAIN_CODE_HAS_EXIST(10016,"火车编号已经存在"),
    TRAIN_CARRIAGE_HAS_EXIST(10017,"火车车厢已经存在"),
    TRAIN_STATION_HAS_EXIST(10018,"火车车站已经存在"),
    CREATE_TASK_ERROR(10019,"任务创建失败"),
    STOP_TASK_ERROR(10020,"暂停定时任务失败:调度异常"),
    RESTART_TASK_ERROR(10021,"重启定时任务失败"),
    RESCHEDULE_TASK_ERROR(10022,"更新定时任务失败"),
    DELETE_TASK_ERROR(10023,"删除定时任务失败:调度异常"),
    QUERY_TASK_ERROR(10024,"查看定时任务失败:调度异常"),
    DAILY_TRAIN_STATION_HAS_EXIST(10025,"今日火车车站已经存在"),
    DAILY_TRAIN_CODE_HAS_EXIST(10026,"今日火车编号已经存在"),
    DAILY_TRAIN_CARRIAGE_HAS_EXIST(10027,"今日火车车厢已经存在"),
    ;


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
