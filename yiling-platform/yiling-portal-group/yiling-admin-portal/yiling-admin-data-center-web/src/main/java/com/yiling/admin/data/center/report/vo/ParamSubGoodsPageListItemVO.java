package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ParamSubGoodsPageListItemVO extends BaseVO {


    /**
     * 对应以岭的商品id
     */
    @ApiModelProperty("对应以岭的商品id")
    private Long ylGoodsId;

    /**
     * 商业商品内码
     */
    @ApiModelProperty("商业商品内码")
    private String goodsInSn;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @ApiModelProperty("商品规格")
    private String goodsSpecification;

    /**
     * 参数类型：1-商品类型 2-促销活动 3-阶梯规则  4-会员返利
     */
    @ApiModelProperty("参数类型：1-商品类型 2-促销活动 3-阶梯规则  4-会员返利")
    private Integer parType;


    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称--仅参数类型为促销时有意义")
    private String activityName;

    /**
     * 商家eid
     */
    @ApiModelProperty(value = "商家eid")
    private Long eid;

    /**
     * 商业名称
     */
    @ApiModelProperty("商业名称")
    private String eName;

    /**
     * 奖励类型：1-金额 2-百分比
     */
    @ApiModelProperty("奖励类型：1-金额 2-百分比--用于判断返利金额展示方法如：元/合&百分比")
    private Integer rewardType;

    /**
     * 门槛数量（仅参数类型为阶梯时有意义）
     */
    @ApiModelProperty("起步数量--仅参数类型为阶梯时有意义")
    private Integer thresholdCount;

    /**
     * 奖励金额
     */
    @ApiModelProperty("奖励金额--仅奖励类型为金额时有意义")
    private BigDecimal rewardAmount;

    /**
     * 奖励百分比
     */
    @ApiModelProperty("奖励百分比--仅奖励类型为百分比时有意义")
    private BigDecimal rewardPercentage;

    /**
     * 活动&阶梯的订单来源：0-全部 1-B2B 2-自建平台 3-三方平台及其他渠道订单
     */
    @ApiModelProperty("活动&阶梯的订单来源：0-全部 1-B2B 2-自建平台 3-三方平台及其他渠道订单")
    private Integer orderSource;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("操作时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @JsonIgnore
    @ApiModelProperty(value = "修改人", hidden = true)
    private Long updateUser;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String updateUserName;
}
