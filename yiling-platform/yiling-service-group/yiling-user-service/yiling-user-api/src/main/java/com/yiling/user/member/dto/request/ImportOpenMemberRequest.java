package com.yiling.user.member.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * 导入开通会员 Request
 *
 * @author: lun.yu
 * @date: 2022-10-08
 */
@Data
public class ImportOpenMemberRequest extends BaseRequest {

    /**
     * 终端ID
     */
    private Long eid;

    /**
     * 终端名称
     */
    private String ename;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 购买条件ID
     */
    private Long buyStageId;

    /**
     * 购买规则
     */
    private String buyRule;

    /**
     * 推广方ID
     */
    private Long promoterId;

    /**
     * 推广方名称
     */
    private String promoterName;

    /**
     * 推广人ID
     */
    private Long promoterUserId;

    /**
     * 推广人名称
     */
    private String promoterUserName;

    /**
     * 导入类型
     */
    private Integer source;

}
