package com.yiling.hmc.welfare.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseListDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;
    /**
     * 商家id
     */
    private Long eid;
    /**
     * 商家名称
     */
    private String ename;
}
