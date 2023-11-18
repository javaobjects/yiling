package com.yiling.settlement.report.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("report_detail_flow")
public class ReportDetailFlowDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 报表eid
     */
    private Long reportId;

    /**
     * 卖家eid
     */
    private Long sellerEid;

    /**
     * op库主键
     */
    private String soId;

    /**
     * Erp销售订单号
     */
    private String soNo;

    /**
     * 订单来源，字典：erp_sale_flow_source，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    private String soSource;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String soSpecifications;

    /**
     * 批号
     */
    private String soBatchNo;

    /**
     * 客户编码（客户内码）
     */
    private String enterpriseInnerCode;

    /**
     * 客户名称
     */
    private String enterpriseName;

    /**
     * 销售数量
     */
    private BigDecimal soQuantity;

    /**
     * 商品单位
     */
    private String soUnit;

    /**
     * 单价
     */
    private BigDecimal soPrice;

    /**
     * 金额
     */
    private BigDecimal soTotalAmount;

    /**
     * 商品生产厂家
     */
    private String soManufacturer;

    /**
     * 生产日期
     */
    private Date soProductTime;

    /**
     * 有效日期
     */
    private Date soEffectiveTime;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 批准文号
     */
    private String soLicense;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 对应以岭的商品id
     */
    private Long ylGoodsId;

    /**
     * 以岭商品名称
     */
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    private String ylGoodsSpecification;

    /**
     * 供货价
     */
    private BigDecimal supplyPrice;

    /**
     * 出货价
     */
    private BigDecimal outPrice;

    /**
     *  购进渠道：1-大运河采购 2-京东采购 3-库存不足
     */
    private Integer purchaseChannel;

    /**
     * 销售额金额
     */
    private BigDecimal salesAmount;

    /**
     * 门槛数量（仅参数类型为阶梯时有意义）
     */
    private Integer thresholdCount;

    /**
     * 阶梯活动名称
     */
    private String ladderName;

    /**
     * 阶梯返利金额
     */
    private BigDecimal ladderAmount;

    /**
     * 阶梯开始时间
     */
    private Date ladderStartTime;

    /**
     * 阶梯结束时间
     */
    private Date ladderEndTime;

    /**
     * 小三员活动名称
     */
    private String xsyName;

    /**
     * 小三元基础奖励单价
     */
    private BigDecimal xsyPrice;

    /**
     * 小三员奖励金额
     */
    private BigDecimal xsyAmount;

    /**
     * 小三员奖励类型：1-金额 2-百分比
     */
    private Integer xsyRewardType;

    /**
     * 小三员开始时间
     */
    private Date xsyStartTime;

    /**
     * 小三员结束时间
     */
    private Date xsyEndTime;

    /**
     * 特殊活动1名称
     */
    private String actFirstName;

    /**
     * 特殊活动1金额
     */
    private BigDecimal actFirstAmount;

    /**
     * 特殊活动1始时间
     */
    private Date actFirstStartTime;

    /**
     * 特殊活动1结束时间
     */
    private Date actFirstEndTime;

    /**
     * 特殊活动2名称
     */
    private String actSecondName;

    /**
     * 特殊活动2金额
     */
    private BigDecimal actSecondAmount;

    /**
     * 特殊活动2始时间
     */
    private Date actSecondStartTime;

    /**
     * 特殊活动2结束时间
     */
    private Date actSecondEndTime;

    /**
     * 特殊活动3名称
     */
    private String actThirdName;

    /**
     * 特殊活动3金额
     */
    private BigDecimal actThirdAmount;

    /**
     * 特殊活动3始时间
     */
    private Date actThirdStartTime;

    /**
     * 特殊活动3结束时间
     */
    private Date actThirdEndTime;

    /**
     * 特殊活动4名称
     */
    private String actFourthName;

    /**
     * 特殊活动4金额
     */
    private BigDecimal actFourthAmount;

    /**
     * 特殊活动4始时间
     */
    private Date actFourthStartTime;

    /**
     * 特殊活动4结束时间
     */
    private Date actFourthEndTime;

    /**
     * 特殊活动5名称
     */
    private String actFifthName;

    /**
     * 特殊活动5金额
     */
    private BigDecimal actFifthAmount;

    /**
     * 特殊活动5始时间
     */
    private Date actFifthStartTime;

    /**
     * 特殊活动5结束时间
     */
    private Date actFifthEndTime;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;

    /**
     * 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
     */
    private Integer abnormalReason;

    /**
     * 异常描述
     */
    private String abnormalDescribed;

    /**
     * 返利金额小计
     */
    private BigDecimal subRebate;

    /**
     * 返利状态：1-待返利 2-已返利 3-部分返利
     */
    private Integer rebateStatus;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}