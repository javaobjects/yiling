package com.yiling.f2b.admin.agreementv2.vo;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议列表项 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementListItemVO extends BaseVO {

    /**
     * 协议编号
     */
    @ApiModelProperty("协议编号")
    private String agreementNo;

    /**
     * 甲方名称
     */
    @ApiModelProperty("甲方名称")
    private String ename;

    /**
     * 乙方名称
     */
    @ApiModelProperty("乙方名称")
    private String secondName;

    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private Date createTime;

    /**
     * 生效时间
     */
    @ApiModelProperty("生效时间")
    private Date startTime;

    /**
     * 失效时间
     */
    @ApiModelProperty("失效时间")
    private Date endTime;

    /**
     * 审核通过日期
     */
    @ApiModelProperty("审核通过日期")
    private Date authTime;

    /**
     * 甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商
     */
    @ApiModelProperty("甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商")
    private Integer firstType;

    /**
     * 甲方协议签订人名称
     */
    @ApiModelProperty("甲方协议签订人名称")
    private String firstSignUserName;

    /**
     * 甲方协议签订人手机号
     */
    @ApiModelProperty("甲方协议签订人手机号")
    private String firstSignUserPhone;

    /**
     * 乙方类型
     */
    @ApiModelProperty("乙方类型（默认就为商业-供应商）")
    private String secondType;

    /**
     * 乙方协议签订人名称
     */
    @ApiModelProperty("乙方协议签订人名称")
    private String secondSignUserName;

    /**
     * 乙方协议签订人手机号
     */
    @ApiModelProperty("乙方协议签订人手机号")
    private String secondSignUserPhone;

    /**
     * 协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议
     */
    @ApiModelProperty("协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议")
    private Integer agreementType;

    /**
     * 付款方式
     */
    @ApiModelProperty("付款方式")
    private List<String> payMethodList;

    /**
     * 结算方式
     */
    @ApiModelProperty("结算方式")
    private List<String> settlementMethodList;

    /**
     * 返利兑付方式：1-电汇 2-冲红 3-票折 4-易货 5-3个月承兑 6-6个月承兑 7-其他 8-支票
     */
    @ApiModelProperty("返利兑付方式：1-电汇 2-冲红 3-票折 4-易货 5-3个月承兑 6-6个月承兑 7-其他 8-支票（见字典：agreement_rebate_cash_type）")
    private Integer rebateCashType;

    /**
     * 返利兑付时间：1-协议生效月起 2-协议完结月起
     */
    @ApiModelProperty("返利兑付时间：1-协议生效月起 2-协议完结月起（见字典：agreement_rebate_cash_time）")
    private Integer rebateCashTime;

    /**
     * 购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进
     */
    @ApiModelProperty(value = "购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进")
    private Integer buyChannel;

    /**
     * 是否活动协议：0-否 1-是
     */
    @ApiModelProperty("是否活动协议：0-否 1-是")
    private Integer activeFlag;

    /**
     * 协议品种数
     */
    @ApiModelProperty("协议品种数")
    private Integer goodsNumber;

    /**
     * 协议负责人名称
     */
    @ApiModelProperty("协议负责人名称")
    private String mainUserName;

    /**
     * 协议生效状态：1-有效 2-中止 3-作废 4-过期 5-排期
     */
    @ApiModelProperty("协议生效状态：1-有效 2-中止 3-作废 4-过期 5-排期")
    private Integer effectStatus;

    /**
     * 是否全系列品种：0-否 1-是
     */
    @ApiModelProperty(value = "是否全系列品种：0-否 1-是")
    private Integer allLevelKindsFlag;

}
