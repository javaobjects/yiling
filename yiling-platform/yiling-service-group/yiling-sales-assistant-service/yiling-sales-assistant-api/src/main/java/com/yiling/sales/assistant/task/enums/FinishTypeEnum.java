package com.yiling.sales.assistant.task.enums;

import java.util.Arrays;
import java.util.Objects;

import lombok.Getter;

/**
 * 任务完成条件类型 1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广
 * @author gxl
 */
@Getter
public enum FinishTypeEnum {
    MONEY(1,"交易额"),
    AMOUNT(2,"交易量"),
    NEW_ENT(3,"新户推广"),
    SALE(4,"促销推广"),
    MEET(5,"会议推广"),
	ACADEMIC(6,"学术推广"),
    NEW_USER(7,"新人推广"),
    MEMBER_BUY(8,"会员推广-购买"),
    MEMBER_PROMOTION(9,"会员推广-满赠"),
    UPLOAD_ACCOMPANYING_BILL(10,"上传资料"),
    ;
    private Integer code;
    private String desc;

    FinishTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

   public static String getByCode(Integer code){
        FinishTypeEnum  finishTypeEnum = Arrays.stream(FinishTypeEnum.values()).filter(typeEnum -> typeEnum.getCode().equals(code)).findFirst().orElse(null);
        if(Objects.isNull(finishTypeEnum)){
            return null;
        }
        return finishTypeEnum.getDesc();
    }
}
