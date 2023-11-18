package com.yiling.b2b.admin.promotion.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionGoodsGiftUsedVO extends BaseVO {

    @ApiModelProperty("赠品名称")
    private String giftName;
    
    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("员工姓名")
    private String buyerName;

    @ApiModelProperty("员工电话")
    private String buyerTel;

    @ApiModelProperty("企业地址")
    private String address;
}
