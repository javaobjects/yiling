package com.yiling.b2b.app.enterprise.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业采购申请列表 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-18
 */
@Data
@Accessors(chain = true)
public class EnterprisePurchaseApplyListItemVO extends BaseVO {

    /**
     * 供应商企业ID
     */
    @ApiModelProperty("供应商企业ID")
    private Long eid;

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String name;

    /**
     * 店铺Logo
     */
    @ApiModelProperty("店铺Logo")
    private String shopLogo;

    /**
     * 审核状态：1-待审核 2-已建采 3-已驳回
     */
    @ApiModelProperty("审核状态：1-待审核 2-已建采 3-已驳回")
    private Integer authStatus;

}
