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
 * @date: 2021/10/25
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityPageVo extends BaseVO {

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
     * 优惠券活动名称
     */
    @ApiModelProperty("优惠券活动名称")
    private String name;

    /**
     * 预算编号
     */
    @ApiModelProperty("预算编号")
    private String budgetCode;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    @ApiModelProperty("活动分类（1-平台活动；2-商家活动）")
    private Integer sponsorType;

    /**
     * 费用承担方（1-平台；2-商家；3-共同承担）
     */
    @ApiModelProperty("费用承担方（1-平台；2-商家；3-共同承担）")
    private Integer bear;

    /**
     * 优惠券类型（1-满减券；2-满折券）
     */
    @ApiModelProperty("优惠券类型（1-满减券；2-满折券）")
    private Integer type;

    /**
     * 优惠规则
     */
    @ApiModelProperty("优惠规则")
    private String couponRules;

    /**
     * 用券时间（1-固定时间；2-发放领取后生效）
     */
    @ApiModelProperty("用券时间（1-固定时间；2-发放领取后生效）")
    private Integer useDateType;

    /**
     * 用券开始时间
     */
    @ApiModelProperty("用券开始时间")
    private Date beginTime;

    /**
     * 用券结束时间
     */
    @ApiModelProperty("用券结束时间")
    private Date endTime;

    /**
     * 优惠券总数量
     */
    @ApiModelProperty("优惠券总数量")
    private Integer totalCount;

    /**
     * 优惠券已发放数量
     */
    @ApiModelProperty("优惠券已发放数量")
    private Integer giveCount = 0;

    /**
     * 优惠券已使用数量
     */
    @ApiModelProperty("优惠券已使用数量")
    private Integer useCount;

    /**
     * 状态（1-启用 2-停用 3-废弃）
     */
    @ApiModelProperty("状态（1-启用 2-停用 3-废弃）")
    private Integer status;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
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
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 发放后生效规则，按发放/领取XX天过期
     */
    @ApiModelProperty("发放后生效规则，按发放/领取XX天过期")
    private String giveOutEffectiveRules;

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

    /**
     * 发放标识（true-可发放；false-不可发放）
     */
    @ApiModelProperty("发放标识（true-可发放；false-不可发放）")
    private Boolean giveFlag;

    /**
     * 所属企业标识（true-自己企业；false-其他企业，只能查看）
     */
    @ApiModelProperty("所属企业标识（true-自己企业；false-其他企业，只能查看）")
    private Boolean enterpriseFlag;

    /**
     * 复制标识（true-可复制；false-不可复制）
     */
    @ApiModelProperty("复制标识（true-可复制；false-不可复制）")
    private Boolean copyFlag;

    /**
     * 增券标识（true-可增券；false-不可增券）
     */
    @ApiModelProperty("增券标识（true-可增券；false-不可增券）")
    private Boolean increaseFlag;

    /**
     * 优惠券类型：1-商品优惠券 2-会员优惠券
     */
    @ApiModelProperty("优惠券类型：1-商品优惠券 2-会员优惠券")
    private Integer memberType;

    /**
     * 1全部会员方案 ，2部分会员方案
     */
    @ApiModelProperty("1全部会员方案 ，2部分会员方案")
    private Integer memberLimit;

    /**
     * 剩余发放数量
     */
    @ApiModelProperty("剩余发放数量")
    private Integer surplusCount;

    /**
     * 面值--供积分模块使用
     */
    @ApiModelProperty("面值--供积分模块使用")
    private String faceValue;

    /**
     * 活动状态（1-未开始 2-进行中 3-已结束）
     */
    @ApiModelProperty("活动状态（1-未开始 2-进行中 3-已结束）")
    private Integer activityStatus;

}
