package com.yiling.sales.assistant.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  销售助手异常码
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.enums
 * @date: 2021/9/10
 */
@Getter
@AllArgsConstructor
public enum AssistantErrorCode implements IErrorCode {
    /**
     * 团队管理
     */
    NOT_LOOK_TEAM(106001, "只有队长才能查看队员"),
    REPEAT_INVITE(106002, "该用户已有人维护，禁止重复邀请"),
    ANY_TEAM_MEMBER(106003, "您还不是任何团队队员"),
    MEMBER_PHONE_STOP(106004, "手机号已停用，信息无法接收"),
    NOT_COULD_INVITE_OWNER(106005, "不能邀请自己为队员"),
    ;

    private final Integer code;
    private final String  message;

}
