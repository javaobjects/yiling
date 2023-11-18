package com.yiling.user.member.bo;

import java.io.Serializable;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-企业的所有会员 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-08
 */
@Data
@Accessors(chain = true)
public class MemberEnterpriseBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前是否为会员身份
     */
    private Boolean memberFlag;

    /**
     * 当前会员ID
     */
    private Long memberId;

    /**
     * 当前会员名称
     */
    private String memberName;

    /**
     * 会员开通时间
     */
    private Date startTime;

    /**
     * 会员到期时间
     */
    private Date endTime;

    /**
     * 背景图
     */
    private String bgPicture;

    /**
     * 会员点亮图
     */
    private String lightPicture;

    /**
     * 会员熄灭图
     */
    private String extinguishPicture;

}
