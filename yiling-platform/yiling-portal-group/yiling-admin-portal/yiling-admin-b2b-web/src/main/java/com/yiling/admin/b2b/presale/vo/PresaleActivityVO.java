package com.yiling.admin.b2b.presale.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动主表
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleActivityVO extends BaseVO {

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("活动类型（1-策略满赠,2-支付促销）")
    private Integer type;

    @ApiModelProperty("活动分类（1-平台活动；2-商家活动；）")
    private Integer sponsorType;

    @ApiModelProperty("生效开始时间")
    private Date beginTime;

    @ApiModelProperty("生效结束时间")
    private Date endTime;
    
    @ApiModelProperty("状态：1-启用 2-停用 3-废弃")
    private Integer status;

    @ApiModelProperty("运营备注")
    private String operatingRemark;

    @ApiModelProperty("费用承担方（1-平台承担；2-商家承担；）")
    private Integer bear;
    
    @ApiModelProperty("平台承担比例")
    private BigDecimal platformRatio;
    
    @ApiModelProperty("商家承担比例")
    private BigDecimal businessRatio;
    @ApiModelProperty("选择平台（1-B2B；2-销售助手）")
    private List<String> platformSelected;

    @ApiModelProperty("商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）")
    private Integer conditionBuyerType;

    @ApiModelProperty("指定企业类型(1:全部类型 2:指定类型)")
    private Integer conditionEnterpriseType;

    @ApiModelProperty("指定企业类型值，多个值用逗号隔开，字典enterprise_type（3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药店；7-医疗机构；8-诊所）")
    private List<String> conditionEnterpriseTypeValue;

    @ApiModelProperty("指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-指定方案会员；5-指定推广方会员）")
    private Integer conditionUserType;

    @ApiModelProperty("其他(1-新客适用,多个值用逗号隔开)")
    private List<String> conditionOther;

    @ApiModelProperty("预售类型（1-定金预售；2-全款预售）")
    private Integer presaleType;

    @ApiModelProperty("支付定金开始时间")
    private Date depositBeginTime;

    @ApiModelProperty("支付定金结束时间")
    private Date depositEndTime;

    @ApiModelProperty("支付尾款开始时间")
    private Date finalPayBeginTime;
    
    @ApiModelProperty("支付尾款结束时间")
    private Date finalPayEndTime;

    @ApiModelProperty("预算金额")
    private BigDecimal budgetAmount;

    @ApiModelProperty("商品信息")
    private List<PresaleGoodsLimitVO> presaleGoodsLimitForms;

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

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建人手机号")
    private String createUserTel;

    @ApiModelProperty("活动进度 0-全部 1-未开始 2-进行中 3-已结束")
    private Integer progress;

    @ApiModelProperty("活动进度 0-全部 1-未开始 2-进行中 3-已结束")
    private Boolean running;

    @ApiModelProperty("查看标识（true-可；false-不可")
    private Boolean lookFlag;

    @ApiModelProperty("修改标识（true-可；false-不可")
    private Boolean updateFlag;

    @ApiModelProperty("复制标识（true-可；false-不可")
    private Boolean copyFlag;

    @ApiModelProperty("停用标识（true-可；false-不可")
    private Boolean stopFlag;

    @ApiModelProperty("预售订单数量")
    private Long presaleOrderNum;

    @ApiModelProperty("指定客户数量")
    private Long buyerNum;

    @ApiModelProperty("指定方案会员数量")
    private Long memberNum;

    @ApiModelProperty("指定推广方会员数量")
    private Long promoterNnm;
}
