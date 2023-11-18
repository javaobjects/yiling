package com.yiling.user.member.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-创建会员 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateMemberRequest extends BaseRequest {

    /**
     * 会员名称
     */
    private String name;

    /**
     * 会员描述
     */
    private String description;

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

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 获取条件：1-付费 2-活动赠送
     */
    private Integer getType;

    /**
     * 是否续卡提醒：0-否 1-是
     */
    private Integer renewalWarn;

    /**
     * 到期前提醒天数
     */
    private Integer warnDays;

    /**
     * 获得条件集合
     */
    private List<CreateMemberBuyStageRequest> memberBuyStageList;

    /**
     * 会员权益集合
     */
    private List<SaveMemberEquityRelationRequest> memberEquityList;


}
