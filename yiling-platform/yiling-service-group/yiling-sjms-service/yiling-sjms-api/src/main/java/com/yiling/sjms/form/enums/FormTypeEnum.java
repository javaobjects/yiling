package com.yiling.sjms.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础表单类型枚举
 * 
 * @author xuan.zhou
 * @date 2023/2/24
 */
@Getter
@AllArgsConstructor
public enum FormTypeEnum {

    GB_SUBMIT(1, "团购提报"),
    EXTEND_INFO_CHANGE(6,"机构扩展信息修改"),
    GB_CANCEL(2,"团购取消"),
    ENTERPRISE_UPDATE(3,"机构修改"),
    ENTERPRISE_LOCK(4,"机构锁定"),
    ENTERPRISE_UNLOCK(5,"机构解锁"),
    ENTERPRISE_REL_SHIP_CHANGE(7,"机构三者关系变审"),
    ENTERPRISE_ADD(8,"机构新增"),
    GOODS_FLEEING(9,"窜货申报"),
    SALES_APPEAL(10,"销量申诉"),
    GROUP_BUY_COST(11,"团购费用申请"),
    HOSPITAL_MANOR_CHANGE(12,"医院辖区变更"),
    HOSPITAL_PHARMACY_BIND(13,"院外药店关系绑定"),
    FIX_MONTH_FLOW(14,"补传月流向")
    ;

    private Integer code;
    private String name;

    public static FormTypeEnum getByCode(Integer code) {
        for (FormTypeEnum e : FormTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
