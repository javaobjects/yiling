package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/16
 */
@Data
public class GbAppealFormOrderVO extends BaseVO {

    /**
     * 团购表单ID
     */
    @ApiModelProperty(value = "团购表单ID")
    private Long formId;

    /**
     * 团购编号
     */
    @ApiModelProperty(value = "团购编号")
    private String gbNo;

    /**
     * 所属流程：1-团购提报 2-团购取消。字典：form_type
     */
    @ApiModelProperty(value = "所属流程：1-团购提报 2-团购取消。字典：form_type")
    private Integer gbProcess;

    /**
     * 团购月份
     */
    @ApiModelProperty(value = "团购月份")
    private String gbMonth;

    /**
     * 流向月份
     */
    @ApiModelProperty(value = "流向月份")
    private String flowMonth;

    /**
     * 销量计入人工号
     */
    @ApiModelProperty(value = "销量计入人工号")
    private String sellerEmpId;

    /**
     * 销量计入人姓名
     */
    @ApiModelProperty(value = "销量计入人姓名")
    private String sellerEmpName;

    /**
     * 机构区办代码
     */
    @ApiModelProperty(value = "区办代码")
    private String districtCountyCode;

    /**
     * 机构区办
     */
    @ApiModelProperty(value = "区办")
    private String districtCounty;

    /**
     * 团购单位ID
     */
    @ApiModelProperty(value = "团购单位ID")
    private Long customerId;

    /**
     * 团购单位名称
     */
    @ApiModelProperty(value = "团购单位名称")
    private String customerName;

    /**
     * 出库商业ID
     */
    @ApiModelProperty(value = "出库商业ID")
    private Long crmId;

    /**
     * 出库商业名称
     */
    @ApiModelProperty(value = "出库商业名称")
    private String ename;

    /**
     * 出库终端ID
     */
    @ApiModelProperty(value = "出库终端ID")
    private Long orgCrmId;

    /**
     * 出库终端名称
     */
    @ApiModelProperty(value = "出库终端名称")
    private String enterpriseName;

    /**
     * 商品编码
     */
    @ApiModelProperty(value = "商品编码")
    private Long goodsCode;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 团购数量
     */
    @ApiModelProperty(value = "团购数量")
    private BigDecimal gbQuantity;

    /**
     * 团购性质：1-普通团购 2-政府采购
     */
    @ApiModelProperty(value = "团购性质：1-普通团购 2-政府采购")
    private Integer gbType;

    /**
     * 审批状态：10-待提交 20-审批中 200-已通过 201-已驳回，字典：gb_form_status
     *
     * com.yiling.sjms.form.enums.FormStatusEnum
     */
    @ApiModelProperty(value = "审批状态：10-待提交 20-审批中 200-已通过 201-已驳回，字典：gb_form_status")
    private Integer auditStatus;

    /**
     * 复核状态：1-未复核 2-已复核，字典：gb_form_review_status
     */
    @ApiModelProperty(value = "复核状态：1-未复核 2-已复核，字典：gb_form_review_status")
    private Integer checkStatus;

    /**
     * 复核意见
     */
    @ApiModelProperty(value = "复核意见")
    private String gbRemark;

    /**
     * 发起人
     */
    @ApiModelProperty(value = "发起人")
    private String createName;

    /**
     * 提交时间
     */
    @ApiModelProperty(value = "提交时间")
    private Date createTime;

    /**
     * 数据处理状态：1-未处理 2-已处理
     */
    @ApiModelProperty(value = "数据处理状态：1-未处理 2-已处理")
    private Integer execStatus;

}
