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
public enum AgencyLockErrorCode implements IErrorCode {

	//当前机构列表中存在重复的机构
    SAVE_FORM_FAIL(180001, "当前机构列表中存在重复的机构"),
	//数据不存在
    DATA_NOT_FIND(180002, "机构信息锁定数据不存在"),
	//表单数据不存在
    FORM_NOT_FIND(180003, "表单数据不存在"),
	//当前流程状态不允许修改操作
    PROHIBIT_UPDATE(180004, "当前流程状态不允许修改操作"),
	//当前人存在草稿不能新建
    ROUGH_DRAFT_HAS_EXIST(180006, "当前人存在草稿不能新建"),
	//当前人存在草稿不能新建
    ROLE_CHAIN_NEED_UNIQUE(180007, "表单中机构的供应链角色必须一致"),

    EXIST_ENTERPRISE_REL(180010,"当前机构有三者关系，流向打取人不能为空！"),

    EXIST_ENTERPRISE_PURCHASE_CHANNEL(180011,"其他机构的采购渠道为当前商业公司，流向打取人不能为空！"),

    ;

    private final Integer code;
    private final String  message;
}
