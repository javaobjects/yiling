package com.yiling.admin.b2b.member.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-企业会员 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-30
 */
@Data
@Accessors(chain = true)
public class EnterpriseMemberVO extends BaseVO {

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
     * 会员ID
     */
    @ApiModelProperty("会员ID")
    private Long memberId;

    /**
     * 会员名称
     */
    @ApiModelProperty("会员名称")
    private String memberName;

    /**
     * 企业类型
     */
    @ApiModelProperty("企业类型")
    private Integer type;

    /**
     * 社会统一信用代码
     */
    @ApiModelProperty("社会统一信用代码")
    private String licenseNumber;

    /**
     * 联系人
     */
    @ApiModelProperty("联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty("联系人电话")
    private String contactorPhone;

    /**
     * 完整详细地址
     */
    @ApiModelProperty("完整详细地址")
    private String completeAddress;

    /**
     * 企业状态
     */
    @ApiModelProperty("企业状态：1-启用 2-停用")
    private Integer enterpriseStatus;

    /**
     * 首次开通时间
     */
    @ApiModelProperty("首次开通时间")
    private Date openTime;

    /**
     * 会员开通时间
     */
    @ApiModelProperty("会员开通时间")
    private Date startTime;

    /**
     * 会员到期时间
     */
    @ApiModelProperty("会员到期时间")
    private Date endTime;

    /**
     * 会员状态
     */
    @ApiModelProperty("会员状态：1-正常 2-过期")
    private Integer status;

    /**
     * 更新人id
     */
    @ApiModelProperty("更新人id")
    private Long updateUser;

    /**
     * 操作人名称
     */
    @ApiModelProperty("操作人名称")
    private String updateUserName;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
