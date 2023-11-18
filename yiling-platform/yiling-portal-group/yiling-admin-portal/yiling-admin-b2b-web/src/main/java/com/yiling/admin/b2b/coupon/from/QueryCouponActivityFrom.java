package com.yiling.admin.b2b.coupon.from;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryCouponActivityFrom extends QueryPageListForm {

    /**
     * 优惠券ID
     */
    @ApiModelProperty("优惠券ID")
    private Long id;

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 优惠券名称
     */
    @ApiModelProperty("优惠券名称")
    private String name;

    /**
     * 预算编号
     */
    @ApiModelProperty("预算编号")
    private String budgetCode;

    /**
     * 优惠券类型（1-满减券；2-满折券）
     */
    @ApiModelProperty("优惠券活动类型（1-满减券；2-满折券）")
    private Integer type;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    @ApiModelProperty("活动分类（1-平台活动；2-商家活动）")
    private Integer sponsorType;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    @ApiModelProperty("状态：1-启用 2-停用 3-废弃")
    private Integer status;

    /**
     * 活动状态：1-未开始 2-进行中 3-已结束
     */
    @ApiModelProperty("活动状态：1-未开始 2-进行中 3-已结束")
    private Integer activityStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建时间-开始时间
     */
    @ApiModelProperty("创建时间-开始时间")
    private Date createBeginTime;

    /**
     * 创建时间-结束时间
     */
    @ApiModelProperty("创建时间-结束时间")
    private Date createEndTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    /**
     * 优惠券可用企业id
     */
    @ApiModelProperty("优惠券可用企业id")
    private Long availableEid;

    /**
     * 运营备注
     */
    @ApiModelProperty("运营备注")
    private String remark;
}
