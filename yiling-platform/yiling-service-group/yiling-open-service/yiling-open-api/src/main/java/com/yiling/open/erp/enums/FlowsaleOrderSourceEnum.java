package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 销售流向订单来源
 *
 * @author: houjie.sun
 * @date: 2022/3/4
 */
public enum FlowsaleOrderSourceEnum {

    DAYUNHE_PLATFORM("1", "大运河平台销售"),
    POP_PLATFORM("2", "POP平台销售"),
    OTHER_PLATFORM("3", "自建平台销售"),
    YAO_SHI_BANG("4", "药师帮销售"),
    YAO_JING_CAI("5", "药京采销售"),
    XIAO_YAO_YAO("6", "小药药销售"),
    YI_YAO_WANG("7", "壹药网销售"),
    YAO_DOU_WANG("8", "药兜网销售"),
    YUN_CAI("9", "云采销售"),
    GUO_YU("10", "国裕医药在线销售"),
    QUAN_YAO_TONG("11", "全药通销售"),
    YAO_PIN_ZHONG_DUAN("12", "药品终端网销售"),
    MI_KUAI_YAO("13", "觅快药销售"),
    YI_KUAI("14", "一块医药销售"),
    JIAN_KANG_51("15", "51健康网销售"),
    OTHERS("16", "其它渠道销售"),
    THIRD_OTHERS("17", "其它三方渠道");

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    FlowsaleOrderSourceEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static FlowsaleOrderSourceEnum getByCode(Integer code) {
        for (FlowsaleOrderSourceEnum e : FlowsaleOrderSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static FlowsaleOrderSourceEnum getByName(String name) {
        for (FlowsaleOrderSourceEnum e : FlowsaleOrderSourceEnum.values()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

}
