package com.yiling.admin.hmc.enterprise.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * C端保险药品商家提成设置表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-24
 */
@Data
public class GoodsVO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("商家id")
    private Long eid;

    @ApiModelProperty("商家名称")
    private String ename;

    @ApiModelProperty("保险药品id")
    private Long goodsId;

    @ApiModelProperty("保险药品名称")
    private String goodsName;

    @ApiModelProperty("标准库商品id")
    private Long standardId;

    @ApiModelProperty("标准库规格id")
    private Long sellSpecificationsId;

    @ApiModelProperty("商家售卖金额/盒")
    private BigDecimal salePrice;

    @ApiModelProperty("给终端结算额/盒")
    private BigDecimal terminalSettlePrice;

    @ApiModelProperty("商品状态 1上架，2下架")
    private Integer goodsStatus;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;

    // =====================非hmc商品表字段========================

    @ApiModelProperty(value = "规格")
    private String sellSpecifications;
}
