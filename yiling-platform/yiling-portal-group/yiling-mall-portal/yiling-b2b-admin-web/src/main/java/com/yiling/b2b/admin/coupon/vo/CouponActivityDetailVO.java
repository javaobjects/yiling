package com.yiling.b2b.admin.coupon.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 优惠券活动详情VO
 *
 * @author: houjie.sun
 * @date: 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("优惠券活动详情VO")
public class CouponActivityDetailVO extends BaseVO {


    /**
     * 复制的优惠券活动id
     */
    @ApiModelProperty(value = "复制的优惠券活动id")
    private Long oldId;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 类型（1-满减券；2-满折券）
     */
    @ApiModelProperty(value = "类型（1-满减券；2-满折券）")
    private Integer type;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称")
    private String name;

    /**
     * 优惠券描述
     */
    @ApiModelProperty(value = "优惠券描述")
    private String description;

    /**
     * 预算编号
     */
    @ApiModelProperty(value = "预算编号")
    private String budgetCode;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    @ApiModelProperty(value = "活动分类（1-平台活动；2-商家活动）")
    private Integer sponsorType;

    /**
     * 使用门槛（1-支付金额满额）
     */
    @ApiModelProperty(value = "活动类型（1-支付金额满额）")
    private Integer threshold;

    /**
     * 门槛金额/件数
     */
    @ApiModelProperty(value = "门槛金额/件数")
    private BigDecimal thresholdValue;

    /**
     * 优惠内容（金额）
     */
    @ApiModelProperty(value = "优惠内容（金额）")
    private BigDecimal discountValue;

    /**
     * 最高优惠额度（折扣券）
     */
    @ApiModelProperty(value = "最高优惠额度（折扣券）")
    private BigDecimal discountMax;

    /**
     * 费用承担方（1-平台；2-商家；3-共同承担）
     */
    @ApiModelProperty(value = "费用承担方（1-平台；2-商家；3-共同承担）")
    private Integer bear;

    /**
     * 平台承担比例
     */
    @ApiModelProperty(value = "平台承担比例")
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    @ApiModelProperty(value = "商家承担比例")
    private BigDecimal businessRatio;

    /**
     * 有效期（1-固定时间；2-发放领取后生效）
     */
    @ApiModelProperty(value = "有效期（1-固定时间；2-发放领取后生效）")
    private Integer useDateType;

    /**
     * 用券开始时间
     */
    @ApiModelProperty(value = "用券开始时间")
    private Date beginTime;

    /**
     * 用券结束时间
     */
    @ApiModelProperty(value = "用券结束时间")
    private Date endTime;

    /**
     * 发放/领取xx天后失效
     */
    @ApiModelProperty(value = "发放/领取xx天后失效")
    private Integer expiryDays;

    /**
     * 平台限制（1-全部平台；2-部分平台）
     */
    @ApiModelProperty(value = "平台限制（1-全部平台；2-部分平台）")
    private Integer platformLimit;

    /**
     * 选择平台（1-B2B；2-销售助手）
     */
    @ApiModelProperty(value = "选择平台（1-B2B；2-销售助手）")
    private List<String> platformSelectedList;

    /**
     * 支付方式限制（1-全部支付方式；2-部分支付方式）
     */
    @ApiModelProperty(value = "支付方式限制（1-全部支付方式；2-部分支付方式）")
    private Integer payMethodLimit;

    /**
     * 选择支付方式（1-在线支付；2-货到付款；3-账期。2,3现在不支持）
     */
    @ApiModelProperty(value = "选择支付方式（1-在线支付；2-货到付款；3-账期。2,3现在不支持）")
    private List<String> payMethodSelectedList;

    /**
     * 可叠加促销列表（1-满赠）
     */
    @ApiModelProperty(value = "可叠加促销列表（1-满赠）")
    private List<String> coexistPromotionList;

    /**
     * 供应商限制（1-全部供应商；2-部分供应商）
     */
    @ApiModelProperty(value = "供应商限制（1-全部供应商；2-部分供应商）")
    private Integer enterpriseLimit;

    /**
     * 可用商品（1-全部商品可用；2-部分商品可用）
     */
    @ApiModelProperty(value = "可用商品（1-全部商品可用；2-部分商品可用）")
    private Integer goodsLimit;

    /**
     * 生成优惠券总数量
     */
    @ApiModelProperty(value = "生成优惠券总数量")
    private Integer totalCount;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用 3-废弃")
    private Integer status;

    /**
     * 是否删除：0-否 1-是
     */
    @ApiModelProperty(value = "是否删除：0-否 1-是")
    private Integer delFlag;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    /**
     * 修改人姓名
     */
    @ApiModelProperty("修改人姓名")
    private String updateUserName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 发放后生效规则，按发放/领取XX天过期
     */
    @ApiModelProperty("发放后生效规则，按发放/领取XX天过期")
    private String giveOutEffectiveRules;

    /**
     * 是否可领取，商家券（1-用户可领 2-用户不可领）
     */
    private Integer canGet;

    // ****************************** 商家后台 用户领取设置 ******************************

    /**
     * 活动开始时间
     */
    @NotNull
    private Date activityBeginTime;

    /**
     * 活动结束时间
     */
    @NotNull
    private Date activityEndTime;

    /**
     * 可领取数量
     */
    @ApiModelProperty("可领取数量")
    @NotNull
    private Integer giveNum;

    /**
     * 指定企业类型(1-全部；2-指定)
     */
    @ApiModelProperty("指定企业类型(1-全部；2-指定)")
    @NotNull
    private Integer conditionEnterpriseType;

    /**
     * 已选企业类型，多个值用逗号隔开，字典enterprise_type（1-工业；2-商业；3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药房；8-诊所；9-民营医院；10-三级医院；11-二级医院；12-社区中心;13-县镇卫生院;14-社区站/村卫生所;15-县人民/中医院）
     */
    @ApiModelProperty("已选企业类型，多个值用逗号隔开，字典enterprise_type")
    private List<String> conditionEnterpriseTypeValueList;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员；5-部分用户）
     */
    @ApiModelProperty("指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员；5-部分用户）")
    @NotNull
    private Integer conditionUserType;

}
