package com.yiling.sjms.agency.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 机构锁定错误码
 *
 * @author dexi.yao
 * @date 2023/02/27
 */
@Getter
@AllArgsConstructor
public enum AgencyRelationShipChangeErrorCode implements IErrorCode {

	//当前机构列表中存在重复的机构
    SAVE_FORM_FAIL(180001, "表单中机构的供应链角色必须一致"),
	//数据不存在
    LIST_ALREADY_EXIST(180002, "列表中已经存在此机构"),
	//表单数据不存在
    FORM_NOT_FIND(180003, "表单数据不存在"),
	//当前流程状态不允许修改操作
    PROHIBIT_UPDATE(180004, "当前流程状态不允许修改操作"),
    ;

    private final Integer code;
    private final String  message;
}
