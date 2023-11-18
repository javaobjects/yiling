package com.yiling.sales.assistant.task.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 配送商
 * @author: ray
 * @date: 2021/10/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DistributorDTO extends BaseDTO {
    private static final long serialVersionUID = -3628619441693134232L;
    private String name;

    private Integer type;

    private Long distributorEid;

    private Boolean selected;
}