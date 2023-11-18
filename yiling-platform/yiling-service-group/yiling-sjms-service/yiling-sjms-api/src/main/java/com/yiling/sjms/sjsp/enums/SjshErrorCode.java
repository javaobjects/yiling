package com.yiling.sjms.sjsp.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举了一些常用API操作码
 *
 * @author dexi.yao
 * @date 2023/03/03
 */
@Getter
@AllArgsConstructor
public enum SjshErrorCode implements IErrorCode {

    TOO_MANY_DEPT_RELATION(190001, "数据审批部门领导对应关系存在多条对应关系"),
    EMP_DEPT_NOT_FIND(190002, "机构新增或修改流程发起人的部门信息不存在"),
    EMP_PLATE_NOT_FIND(190003, "机构新增或修改流程发起人部门对应的板块信息不存在"),
    APPROVE_USER_NOT_FIND(190004, "机构新增或修改流程发起时没有找到审批人"),
    FORM_TYPE_INVALID(190005, "机构新增或修改流程发时formType非法"),
    ;

    private final Integer code;
    private final String  message;
}
