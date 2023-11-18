package com.yiling.user.enterprise.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业销售区域详情 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseSalesAreaDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 区域编码
     */
    private String areaCode;

}
