package com.yiling.f2b.admin.agreementv2.form;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询协议分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-09
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementPageListForm extends QueryPageListForm {

    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间（即生效开始时间）")
    private Date startTime;

    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间（即生效结束时间）")
    private Date endTime;

    /**
     * 开始创建时间
     */
    @ApiModelProperty(value = "开始创建时间")
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

    /**
     * 乙方名称
     */
    @ApiModelProperty(value = "乙方名称")
    private String secondName;

    /**
     * 返利标准：1-销售 2-购进 3-付款金额
     */
    @ApiModelProperty(value = "返利标准：1-销售 2-购进 3-付款金额（见字典：agreement_rebate_task_standard）")
    private Integer rebateStandard;

    /**
     * 商业运营签订人名称
     */
    @ApiModelProperty("商业运营签订人名称")
    private String businessOperatorName;

    /**
     * 协议编号
     */
    @ApiModelProperty("协议编号")
    private String agreementNo;

    /**
     * 协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议（枚举：AgreementTypeEnum）
     */
    @ApiModelProperty(value = "协议类型：1-一级协议 2-二级协议 3-临时协议 4-商业供货协议 5-KA连锁协议 6-代理商协议（见字典：agreement_type）")
    private Integer agreementType;

    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /**
     * 是否活动协议：0-否 1-是
     */
    @ApiModelProperty("是否活动协议：0-否 1-是")
    private Integer activeFlag;

    /**
     * 协议生效状态
     */
    @ApiModelProperty("协议生效状态")
    private Integer effectStatus;

    /**
     * 协议负责人
     */
    @ApiModelProperty(value = "协议负责人")
    private String mainUserName;

    /**
     * 购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进
     */
    @ApiModelProperty(value = "购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进")
    private Integer buyChannel;

    /**
     * 甲方名称
     */
    @ApiModelProperty(value = "甲方名称")
    private String ename;

    /**
     * 审核通过开始时间
     */
    @ApiModelProperty(value = "审核通过开始时间")
    private Date startAuthPassTime;

    /**
     * 审核通过结束时间
     */
    @ApiModelProperty(value = "审核通过结束时间")
    private Date endAuthPassTime;



}
