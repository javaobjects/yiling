package com.yiling.admin.hmc.insurance.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 拿药计划详情 VO
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceFetchPlanDetailVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 参保记录表id
     */
    @ApiModelProperty("参保记录id")
    private Long insuranceRecordId;

    /**
     * eId
     */
    @ApiModelProperty("eId")
    private Long eId;

    /**
     * goodsId
     */
    @ApiModelProperty("goodsId")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 保司给以岭结算价格
     */
    @ApiModelProperty("保司给以岭结算价格")
    private BigDecimal settlePrice;

    /**
     * 以岭跟终端结算单价
     */
    @ApiModelProperty("以岭跟终端结算单价")
    private BigDecimal terminalSettlePrice;

    /**
     * 每月拿药量
     */
    @ApiModelProperty("每月拿药量")
    private Integer perMonthCount;

    /**
     * 共多少盒
     */
    @ApiModelProperty("共多少盒")
    private Integer totalCount;

    /**
     * 共兑几次
     */
    @ApiModelProperty("共兑几次")
    private Integer totalTimes;

    /**
     * 还剩几次
     */
    @ApiModelProperty("还剩几次")
    private Long leftTimes;

    /**
     * 已兑多少盒
     */
    @ApiModelProperty("已兑多少盒")
    private Long tookTotalCount;

    /**
     * 还剩多少盒
     */
    @ApiModelProperty("还剩多少盒")
    private Long leftTotalCount;

    /**
     * 规格信息
     */
    @ApiModelProperty("规格信息")
    private String specificInfo;

    /**
     * 参保价
     */
    @ApiModelProperty("参保价")
    private BigDecimal insurancePrice;

}
