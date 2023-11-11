package com.wbu.train.common.util;

import lombok.Data;

public enum BusinessType {
    //登录
    TYPE_LOGIN(1);
    private int type;
    private BusinessType(int type){
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
