package com.yiling.user.member.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMemberRequest extends QueryPageListRequest {

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 创建开始时间
     */
    private Date startCreateTime;

    /**
     * 创建结束时间
     */
    private Date endCreateTime;



}
