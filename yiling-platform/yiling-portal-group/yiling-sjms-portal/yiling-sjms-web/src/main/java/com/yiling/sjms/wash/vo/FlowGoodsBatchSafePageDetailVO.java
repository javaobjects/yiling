package com.yiling.sjms.wash.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/3/8
 */
@Data
public class FlowGoodsBatchSafePageDetailVO extends BaseVO {

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "终端机构编码")
    private Long crmEnterpriseId;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String name;

    /**
     * crm商品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private Long crmGoodsCode;

    /**
     * crm商品名称
     */
    @ApiModelProperty(value = "标准产品名称")
    private String crmGoodsName;

    /**
     * crm商品规格
     */
    @ApiModelProperty(value = "标准产品规格")
    private String crmGoodsSpecifications;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private BigDecimal gbNumber;

    /**
     * 商品单位
     */
    @ApiModelProperty(value = "单位")
    private String gbUnit;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String opUser;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "最后操作时间")
    private Date opTime;

}
