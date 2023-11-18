package com.yiling.dataflow.wash.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2022/1/11
 */
@Getter
@AllArgsConstructor
public enum WashErrorEnum implements IErrorCode {

    FLOW_WASH_ERROR(1, "流向清洗异常"),
    FLOW_WASH_MONTH_REPEAT_ERROR(2, "当前月份已设置流向清洗日程"),
    FLOW_WASH_DATA_REPEAT_ERROR(3, "日程数据范围时间和其它日程数据范围时间重叠"),
    FLOW_WASH_GOODS_REPEAT_ERROR(4, "日程流向上传时间和其它日程流向上传时间重叠"),
    FLOW_WASH_CUSTOMER_REPEAT_ERROR(5, "日程客户对照时间和其它日程客户对照时间重叠"),
    FLOW_WASH_GOODSbATCH_REPEAT_ERROR(6, "日程在途订单时间和其它日程在途订单时间重叠"),
    FLOW_WASH_CROSS_REPEAT_ERROR(7, "日程窜货提报时间和其它日程窜货提报重叠"),
    FLOW_WASH_REPEAT_OPEN_ERROR(8, "月流向日程重复启动"),

    FLOW_WASH_FINISH_SEND_REPORT_ERROR(9, "月流向清洗完成后生成报表异常"),

    FLOW_WASH_CONFIRM_ERROR(10, "月流向清洗任务确认异常"),

    FLOW_WASH_CONTROL_STATUS_FINISH(11, "月流向清洗日程已结束"),

    FLOW_WASH_TASK_STATUS_EXCEPTION(12, "月流向任务状态异常"),

    FLOW_WASH_TASK_FLOW_CLASSIFY_EXCEPTION(13, "月流向清洗任务，流向分类异常"),
    FLOW_WASH_TASK_NOT_ENTERPRISE_EXCEPTION(14, "在备份档案里面没有找到对应的企业信息！"),

    FLOW_WASH_CONTROL_CANT_OPERATE(15, "清洗进行中，不支持编辑！"),
    UNLOCK_SALE_WASH_RULE_SORT_EXCEPTION(16, "已经存在相同的排序号"),
    ;

    private final Integer code;
    private final String message;

    public static WashErrorEnum getFromCode(Integer code) {
        for (WashErrorEnum e : WashErrorEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
