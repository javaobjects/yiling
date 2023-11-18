package com.yiling.admin.b2b.coupon.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityEnterpriseVO extends BaseVO {
    /**
     * 创建人名称
     */
    @ApiModelProperty("企业名称")
    private String name;
}
