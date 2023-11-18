package com.yiling.user.member.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.user.member.bo.MemberEquityBO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员详情DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CurrentMemberDTO extends BaseDTO {

    /**
     * 会员名称
     */
    private String name;

    /**
     * 会员描述
     */
    private String description;

    /**
     * 当前用户是否为会员：0-否 1-是
     */
    private Integer currentMember;

    /**
     * 背景图
     */
    private String bgPicture;

    /**
     * 会员开通时间
     */
    private Date startTime;

    /**
     * 会员到期时间
     */
    private Date endTime;

    /**
     * 是否停止获取：0-否 1-是
     */
    private Integer stopGet;

    /**
     * 推广企业ID（只有当前企业为会员时才可能存在此值）
     */
    private Long promoterId;

    /**
     * 推广人ID
     */
    private Long promoterUserId;

    /**
     * 获得条件集合
     */
    private List<MemberBuyStageDTO> memberBuyStageList;

    /**
     * 会员权益集合
     */
    private List<MemberEquityBO> memberEquityList;

}
