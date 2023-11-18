package com.yiling.bi.protocol.enums;

/**
 * @author fucheng.bai
 * @date 2023/1/6
 */
public enum DisplayContentEnum {
    BRACKET("bracket", "端架"),
    PILEHEAD("pilehead", "堆头"),
    FLOWER_CAR("flowerCar", "花车"),
    GT_PILEHEAD("gtPilehead", "柜台堆头"),
    CASH_DESK("cashDesk", "收银台"),
    STUD("stud", "立柱"),
    LAMP_BOX("lampBox", "灯箱"),
    SHOWBILL("showbill", "吊旗"),
    SHOPWINDOW("shopwindow", "橱窗")
    ;


    private String key;
    private String value;

    DisplayContentEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
