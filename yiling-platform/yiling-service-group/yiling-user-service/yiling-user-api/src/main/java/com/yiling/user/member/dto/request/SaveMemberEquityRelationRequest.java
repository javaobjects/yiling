package com.yiling.user.member.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-保存会员权益关系 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveMemberEquityRelationRequest extends BaseRequest {

    /**
     * 会员ID
     */
    @NotNull
    private Long memberId;

    /**
     * 权益ID
     */
    @NotNull
    private Long equityId;

    /**
     * 排序
     */
    private Integer sort;


}
