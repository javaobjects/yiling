package com.yiling.sjms.gb.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 团购商品
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)

public class GbOrgMangeDTO extends BaseDO {


    /**
     * 业务部编码
     */
    private Long orgId;

    /**
     * 业务部名称
     */
    private String orgName;

}
