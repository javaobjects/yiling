package com.yiling.admin.b2b.coupon.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/3
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityAutoGivePageVo extends BaseVO {

    /**
     * 自动发券活动名称
     */
    @ApiModelProperty(value = "自动发券活动名称")
    private String name;

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    private Date beginTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private Date endTime;

    /**
     * 自动发券类型（1-订单累计金额；2-会员自动发券）
     */
    @ApiModelProperty(value = "自动发券类型（1-订单累计金额；2-会员自动发券）")
    private Integer type;

    /**
     * 累计金额/数量（如果是订单累计金额，则表示累计金额。如果是订单累计数量，则表示累计订单数量。）
     */
    @ApiModelProperty(value = "累计金额/数量")
    private Integer cumulative;

    /**
     * 最多发放次数
     */
    @ApiModelProperty(value = "最多发放次数")
    private Integer maxGiveNum;

    /**
     * 已发放数量
     */
    @ApiModelProperty(value = "已发放数量")
    private Integer giveCount = 0;

    /**
     * 剩余数量
     */
    @ApiModelProperty(value = "剩余数量")
    private Integer surplusCount;

    /**
     * 状态（1-启用；2-停用；3-废弃）
     */
    @ApiModelProperty(value = "状态（1-启用；2-停用；3-废弃）")
    private Integer status;

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
     * 活动状态（1-未开始 2-进行中 3-已结束）
     */
    @ApiModelProperty(value = "活动状态（1-未开始 2-进行中 3-已结束）")
    private Integer activityStatus;

    /**
     * 修改标识（true-可修改；false-不可修改）
     */
    @ApiModelProperty("修改标识（true-可修改；false-不可修改）")
    private Boolean updateFlag;

    /**
     * 停用标识（true-可停用；false-不可停用）
     */
    @ApiModelProperty("停用标识（true-可停用；false-不可停用）")
    private Boolean stopFlag;

    /**
     * 作废标识（true-可作废；false-不可作废）
     */
    @ApiModelProperty("作废标识（true-可作废；false-不可作废）")
    private Boolean scrapFlag;

}
