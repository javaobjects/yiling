package com.yiling.user.agreement.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利字典表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateDictionariesDTO extends BaseDTO {


    /**
     * 字典类型code 1-返利种类科目 2-归属执行部门
     */
    private Integer code;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 名称
     */
    private String name;



}
