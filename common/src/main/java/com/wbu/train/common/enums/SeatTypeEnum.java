package com.wbu.train.common.enums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public enum SeatTypeEnum {

    YDZ("1", "一等座", new BigDecimal("0.4")),
    EDZ("2", "二等座", new BigDecimal("0.3")),
    RW("3", "软卧", new BigDecimal("0.6")),
    YW("4", "硬卧", new BigDecimal("0.5"));

    private String key;

    private String value;

    /**
     * 基础票价 N元/公里，0.4即为0.4元/公里
     */
    private BigDecimal price;

    SeatTypeEnum(String key, String value, BigDecimal price) {
        this.key = key;
        this.value = value;
        this.price = price;
    }

    public String getCode() {
        return key;
    }

    public String getDesc() {
        return value;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public static List<HashMap<String,String>> getEnumList() {
        List<HashMap<String, String>> list = new ArrayList<>();
        for (SeatTypeEnum anEnum : EnumSet.allOf(SeatTypeEnum.class)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("key",anEnum.key);
            map.put("value",anEnum.value);
            list.add(map);
        }
        return list;
    }

    public static SeatTypeEnum getEnumByCode(String key) {
        for (SeatTypeEnum enums : SeatTypeEnum.values()) {
            if (enums.getCode().equalsIgnoreCase(key)) {
                return enums;
            }
        }
        return null;
    }
}
