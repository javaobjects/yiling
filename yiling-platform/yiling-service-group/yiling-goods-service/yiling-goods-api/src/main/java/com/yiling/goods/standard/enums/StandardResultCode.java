package com.yiling.goods.standard.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举了一些常用API操作码
 *
 * @author xuan.zhou
 * @date 2019/10/15
 */
@Getter
@AllArgsConstructor
public enum StandardResultCode implements IErrorCode {


    STANDARD_DATA_DUPLICATION(10080, "标准库药品批准文号重复"),
    STANDARD_DATA_CATEGORY_UPDATE(10081, "标准库分类表中数据不存在"),
    STANDARD_PARENT_CATEGORY_UPDATE(10082, "标准库分类表父类id不存在"),
    STANDARD_CATEGORY_NAME_REPEAT(10083, "标准库分类表名称存在"),
    STANDARD_SPECIFICATION_DUPLICATION(10084, "标准库商品规格名或规格条形码重复"),
    STANDARD_TAG_NAME_EXIST(10085,"标准库标签名称已存在"),
    ;

    private final Integer code;
    private final String message;
}
