package com.yiling.marketing.couponactivity.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityEnterpriseGiveDTO extends BaseDTO {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 企业类型
     */
    private Integer etype;

    /**
     * 终端区域编码（所属省份编码）
     */
    private String regionCode;

    /**
     * 终端区域名称（所属省份名称）
     */
    private String regionName;

    /**
     * 认证状态（1-未认证 2-认证通过 3-认证不通过）
     */
    private Integer authStatus;

}
