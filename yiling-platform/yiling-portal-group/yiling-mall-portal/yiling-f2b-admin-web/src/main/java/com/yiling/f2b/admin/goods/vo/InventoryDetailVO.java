package com.yiling.f2b.admin.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 InventoryDetailVO
 * @描述
 * @创建时间 2022/7/28
 * @修改人 shichen
 * @修改时间 2022/7/28
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InventoryDetailVO extends BaseVO {

    /**
     * 订阅企业id
     */
    @ApiModelProperty(value = "企业id")
    private Long subscriptionEid;

    /**
     * 订阅企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String subscriptionEname;

    /**
     * 订阅内码
     */
    @ApiModelProperty(value = "被订阅方内码")
    private String inSn;

    /**
     * 来源内码
     */
    @ApiModelProperty(value = "订阅方内码")
    private String sourceInSn;

    /**
     * 订阅类型 1：本店订阅   2：erp订阅  3：pop订阅
     */
    @ApiModelProperty(value = "订阅类型 1：本店订阅   2：erp订阅  3：pop订阅")
    private Integer subscriptionType;

    /**
     * 可用库存
     */
    @ApiModelProperty(value = "库存")
    private Long qty;
}
