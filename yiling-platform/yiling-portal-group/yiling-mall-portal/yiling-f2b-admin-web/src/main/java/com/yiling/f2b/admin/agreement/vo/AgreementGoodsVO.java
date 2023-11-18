package com.yiling.f2b.admin.agreement.vo;

import java.math.BigDecimal;

import com.yiling.f2b.admin.goods.vo.GoodsDisableVO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementGoodsVO extends BaseVO {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long   goodsId;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 售卖规格
     */
    @ApiModelProperty(value = "售卖规格")
    private String sellSpecifications;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "批准文号")
    private String licenseNo;

    /**
     * 基价
     */
    @ApiModelProperty(value = "基价")
    private BigDecimal price;

    /**
     * 专利类型 1-非专利 2-专利
     */
    @ApiModelProperty(value = "专利类型 1-非专利 2-专利")
    private Integer isPatent;

    /**
     * 标准库Id
     */
    @ApiModelProperty(value = "标准库Id")
    private Long standardId;

    /**
     * 销售规格Id
     */
    @ApiModelProperty(value = "销售规格Id")
    private Long sellSpecificationsId;

    @ApiModelProperty(value = "是否已经包含其它属性（协议）")
    private GoodsDisableVO goodsDisableVO;
}
