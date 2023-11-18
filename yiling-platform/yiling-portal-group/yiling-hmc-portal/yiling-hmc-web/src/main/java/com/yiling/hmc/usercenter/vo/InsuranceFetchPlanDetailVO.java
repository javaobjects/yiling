package com.yiling.hmc.usercenter.vo;

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
     * 图片
     */
    @ApiModelProperty("图片")
    private String pic;


    /**
     * 参保价
     */
    @ApiModelProperty("参保价")
    private BigDecimal insurancePrice;

}
