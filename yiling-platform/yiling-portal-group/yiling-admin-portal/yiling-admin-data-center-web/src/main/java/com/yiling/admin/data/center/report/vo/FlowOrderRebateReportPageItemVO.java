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
 * 订单返利报表
 *
 * @author:wei.wang
 * @date:2021/6/18
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowOrderRebateReportPageItemVO extends BaseVO {


    /**
     * 商业代码（商家eid）
     */
    @ApiModelProperty(value = "商业代码（商家eid）")
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    @ApiModelProperty(value = "商业名称（商家名称）")
    private String ename;

    /**
     * Erp销售订单号
     */
    @ApiModelProperty(value = "Erp销售订单号")
    private String soNo;

    /**
     * 订单来源，字典：erp_sale_flow_source，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    @JsonIgnore
    @ApiModelProperty(value = "订单来源，字典：erp_sale_flow_source，1-大运河销售 2-自建平台销售 3-其它渠道销售",hidden = true)
    private String soSource;

    /**
     * 订单来源，字典：erp_sale_flow_source，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    @ApiModelProperty(value = "订单来源，字典：erp_sale_flow_source，1-大运河销售 2-自建平台销售 3-其它渠道销售")
    private String soSourceStr;

    /**
     * 销售日期
     */
    @ApiModelProperty(value = "销售日期")
    private Date soTime;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String soSpecifications;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号")
    private String soBatchNo;

    /**
     * 客户编码（客户内码）
     */
    @ApiModelProperty(value = "客户编码（客户内码）")
    private String enterpriseInnerCode;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String enterpriseName;

    /**
     * 销售数量
     */
    @ApiModelProperty(value = "销售数量")
    private BigDecimal soQuantity;

    /**
     * 商品单位
     */
    @ApiModelProperty(value = "商品单位")
    private String soUnit;

    /**
     * 价格
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal soPrice;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal soTotalAmount;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "商品生产厂家")
    private String soManufacturer;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    private Date soProductTime;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private Date soEffectiveTime;

    /**
     * 商品内码
     */
    @ApiModelProperty(value = "商品内码")
    private String goodsInSn;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "批准文号")
    private String soLicense;

    /**
     * 所属省份名称
     */
    @ApiModelProperty(value = "所属省份名称")
    private String provinceName;

    /**
     * 所属城市名称
     */
    @ApiModelProperty(value = "所属城市名称")
    private String cityName;

    /**
     * 所属区域名称
     */
    @ApiModelProperty(value = "所属区域名称")
    private String regionName;


    /**
     * 以岭商品ID
     */
    @ApiModelProperty(value = "以岭商品ID")
    private Long ylGoodsId;


    /**
     * 以岭商品名称
     */
    @ApiModelProperty(value = "以岭商品名称")
    private String ylGoodsName;

    /**
     * 以岭商品名称
     */
    @ApiModelProperty(value = "以岭商品名称")
    private String ylSpecifications;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;

    /**
     * 出货价
     */
    @ApiModelProperty(value = "出货价")
    private BigDecimal outPrice;

    /**
     * 动销渠道：1-大运河平台销售
     */
    @ApiModelProperty(value = "动销渠道：1-大运河平台销售")
    private Integer syncPurChannel=1;

    /**
     * 门槛数量（仅参数类型为阶梯时有意义）
     */
    @ApiModelProperty(value = "起步数量")
    private Integer thresholdCount;

    /**
     * 阶梯活动名称
     */
    @ApiModelProperty(value = "阶梯活动名称")
    private String ladderName;

    /**
     * 阶梯返利金额
     */
    @ApiModelProperty(value = "阶梯返利金额")
    private BigDecimal ladderAmount;

    /**
     * 阶梯开始时间
     */
    @ApiModelProperty(value = "阶梯开始时间")
    private Date ladderStartTime;

    /**
     * 阶梯结束时间
     */
    @ApiModelProperty(value = "阶梯结束时间")
    private Date ladderEndTime;

    /**
     * 小三员活动名称
     */
    @ApiModelProperty(value = "小三员活动名称")
    private String xsyName;

    /**
     * 小三元基础奖励单价
     */
    @ApiModelProperty(value = "小三元基础奖励单价")
    private BigDecimal xsyPrice;

    /**
     * 小三员奖励金额
     */
    @ApiModelProperty(value = "小三员奖励金额")
    private BigDecimal xsyAmount;

    /**
     * 小三员奖励类型：1-金额 2-百分比
     */
    @ApiModelProperty(value = "小三员奖励类型：1-金额 2-百分比")
    private Integer xsyRewardType;

    /**
     * 小三员开始时间
     */
    @ApiModelProperty(value = "小三员开始时间")
    private Date xsyStartTime;

    /**
     * 小三员结束时间
     */
    @ApiModelProperty(value = "小三员结束时间")
    private Date xsyEndTime;

    /**
     * 特殊活动1名称
     */
    @ApiModelProperty(value = "特殊活动1名称")
    private String actFirstName;

    /**
     * 特殊活动1金额
     */
    @ApiModelProperty(value = "特殊活动1金额")
    private BigDecimal actFirstAmount;

    /**
     * 特殊活动1始时间
     */
    @ApiModelProperty(value = "特殊活动1始时间")
    private Date actFirstStartTime;

    /**
     * 特殊活动1结束时间
     */
    @ApiModelProperty(value = "特殊活动1结束时间")
    private Date actFirstEndTime;

    /**
     * 特殊活动2名称
     */
    @ApiModelProperty(value = "特殊活动2名称")
    private String actSecondName;

    /**
     * 特殊活动2金额
     */
    @ApiModelProperty(value = "特殊活动2金额")
    private BigDecimal actSecondAmount;

    /**
     * 特殊活动2始时间
     */
    @ApiModelProperty(value = "特殊活动2始时间")
    private Date actSecondStartTime;

    /**
     * 特殊活动2结束时间
     */
    @ApiModelProperty(value = "特殊活动2结束时间")
    private Date actSecondEndTime;

    /**
     * 特殊活动3名称
     */
    @ApiModelProperty(value = "特殊活动3名称")
    private String actThirdName;

    /**
     * 特殊活动3金额
     */
    @ApiModelProperty(value = "特殊活动3金额")
    private BigDecimal actThirdAmount;

    /**
     * 特殊活动3始时间
     */
    @ApiModelProperty(value = "特殊活动3始时间")
    private Date actThirdStartTime;

    /**
     * 特殊活动3结束时间
     */
    @ApiModelProperty(value = "特殊活动3结束时间")
    private Date actThirdEndTime;

    /**
     * 特殊活动4名称
     */
    @ApiModelProperty(value = "特殊活动4名称")
    private String actFourthName;

    /**
     * 特殊活动4金额
     */
    @ApiModelProperty(value = "特殊活动4金额")
    private BigDecimal actFourthAmount;

    /**
     * 特殊活动4始时间
     */
    @ApiModelProperty(value = "特殊活动4始时间")
    private Date actFourthStartTime;

    /**
     * 特殊活动4结束时间
     */
    @ApiModelProperty(value = "特殊活动4结束时间")
    private Date actFourthEndTime;

    /**
     * 特殊活动5名称
     */
    @ApiModelProperty(value = "特殊活动5名称")
    private String actFifthName;

    /**
     * 特殊活动5金额
     */
    @ApiModelProperty(value = "特殊活动5金额")
    private BigDecimal actFifthAmount;

    /**
     * 特殊活动5始时间
     */
    @ApiModelProperty(value = "特殊活动5始时间")
    private Date actFifthStartTime;

    /**
     * 特殊活动5结束时间
     */
    @ApiModelProperty(value = "特殊活动5结束时间")
    private Date actFifthEndTime;

    /**
     * 报表状态：0-待返利 1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回
     */
    @ApiModelProperty(value = "报表状态：0-待返利 1-待运营确认 2-待财务确认 3-财务已确认 4-运营驳回 5-财务驳回 6-管理员驳回")
    private Integer reportStatus;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    @ApiModelProperty(value = "标识状态：1-正常订单,2-无效订单,3-异常订单")
    private Integer identificationStatus;

    /**
     * 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
     */
    @ApiModelProperty(value = "异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他")
    private Integer abnormalReason;

    /**
     * 异常描述
     */
    @ApiModelProperty(value = "异常描述")
    private String abnormalDescribed;

}
