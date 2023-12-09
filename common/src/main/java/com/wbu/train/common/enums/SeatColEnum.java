package com.wbu.train.common.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum SeatColEnum {

    YDZ_A("A", "A", "1"),
    YDZ_C("C", "C", "1"),
    YDZ_D("D", "D", "1"),
    YDZ_F("F", "F", "1"),
    EDZ_A("A", "A", "2"),
    EDZ_B("B", "B", "2"),
    EDZ_C("C", "C", "2"),
    EDZ_D("D", "D", "2"),
    EDZ_F("F", "F", "2"),
    RW_A("A", "A", "3"),
    RW_B("B", "B", "3"),
    RW_C("C", "C", "3"),
    YW_A("A", "A", "4"),
    YW_B("B", "B", "4"),
    YW_C("C", "C", "4"),
    YW_D("D", "D", "4");


    private String key;

    private String value;

    /**
     * 对应SeatTypeEnum.key
     */
    private String type;

    SeatColEnum(String key, String value, String type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public String getCode() {
        return key;
    }

    public void setCode(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 根据车箱的座位类型，筛选出所有的列，比如车箱类型是一等座，则筛选出columnList={ACDF}
     */
    public static List<SeatColEnum> getColsByType(String seatType) {
        List<SeatColEnum> colList = new ArrayList<>();
        EnumSet<SeatColEnum> seatColEnums = EnumSet.allOf(SeatColEnum.class);
        for (SeatColEnum anEnum : seatColEnums) {
            if (seatType.equals(anEnum.getType())) {
                colList.add(anEnum);
            }
        }
        return colList;
    }

}
