package com.yiling.admin.b2b.coupon.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("自主领券券活动详情VO")
public class CouponActivityAutoGetDetailVO  extends BaseVO {

    /**
     * 自主领券活动名称
     */
    @ApiModelProperty(value = "自主领券活动名称", position = 2)
    private String name;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间", position = 3)
    private Date beginTime;

    /**
     * 客户范围：1-全部客户 2-指定客户 3指定范围客户
     */
    private Integer conditionEnterpriseRange;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动开始时间", position = 4)
    private Date endTime;

    /**
     * 指定企业类型(1:全部 2:指定)
     */
    @ApiModelProperty(value = "指定企业类型(1:全部 2:指定)", position = 5)
    private Integer conditionEnterpriseType;

    /**
     * 已选企业类型，多个值用逗号隔开，字典enterprise_type（1-工业；2-商业；3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药房；8-诊所；9-民营医院；10-三级医院；11-二级医院；12-社区中心;13-县镇卫生院;14-社区站/村卫生所;15-县人民/中医院）
     */
    @ApiModelProperty(value = "已选企业类型，多个值用逗号隔开，字典enterprise_type", position = 6)
    private List<String> conditionEnterpriseTypeValueList;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员）
     */
    @ApiModelProperty(value = "指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员）", position = 7)
    private Integer conditionUserType;

    /**
     * 状态：1-启用 2-停用 3-作废
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用 3-作废", position = 8)
    private Integer status;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", position = 9)
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", position = 10)
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人", position = 11)
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", position = 12)
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
    @ApiModelProperty(value = "备注", position = 14)
    private String remark;

    /**
     * 关联优惠券列表
     */
    @ApiModelProperty(value = "关联优惠券列表", position = 15)
    private List<CouponActivityAutoGetCouponDetailVO> couponActivityList;

    /**
     * 活动是否已开始：true-已开始 false-未开始
     */
    @ApiModelProperty(value = "活动是否已开始：true-已开始 false-未开始")
    private Boolean running;


}
