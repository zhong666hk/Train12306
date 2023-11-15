package com.wbu.train.member.enums;

public enum PassengerTypeEnum {
    ADULT("1","成人"),
    CHILD("2","儿童"),
    STUDENT("3","学生")
    ;
    private String code;
    private String desc;

    PassengerTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
