package com.yiling.admin.erp.enterprise.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/10/19
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("采购异常信息VO")
public class ErpMonitorPurchaseExceptionVO extends BaseVO {

    /**
     * 采购企业ID
     */
    @ApiModelProperty(value = "采购企业ID")
    private Long eid;

    /**
     * 采购企业名称
     */
    @ApiModelProperty(value = "采购企业名称")
    private String ename;

    /**
     * 供应商eid
     */
    @ApiModelProperty(value = "供应商eid")
    private Long supplierId;

    /**
     * 采购时间
     */
    @ApiModelProperty(value = "采购时间")
    private Date poTime;

    /**
     * 商品规格id
     */
    @ApiModelProperty(value = "商品规格id")
    private Long specificationId;

    /**
     * 采购数量小计
     */
    @ApiModelProperty(value = "采购数量小计")
    private BigDecimal totalPoQuantity;

    /**
     * 销售数量小计
     */
    @ApiModelProperty(value = "销售数量小计")
    private BigDecimal totalSoQuantity;

    /**
     * 有无销售：1-无销售，2-有销售-数量不符合(采购数量>销售数量)，3-有销售-数量符合（字典：erp_purchase_sale_flag）
     */
    @ApiModelProperty(value = "有无销售：1-无销售，2-有销售-数量不符合(采购数量>销售数量)，3-有销售-数量符合（字典：erp_purchase_sale_flag）")
    private Integer saleFlag;


}
