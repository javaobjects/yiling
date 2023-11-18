package com.yiling.user.member.bo;

import java.io.Serializable;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员到期提醒 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-25
 */
@Data
@Accessors(chain = true)
public class MemberExpiredBO implements Serializable {

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 到期时间
     */
    private Date expiredDate;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 会员开通时间
     */
    private Date startTime;

    /**
     * 会员到期时间
     */
    private Date endTime;

    /**
     * 到期前提醒天数
     */
    private Integer warnDays;

}
