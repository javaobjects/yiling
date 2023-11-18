package com.yiling.user.member.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-更新会员排序 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMemberSortRequest extends BaseRequest {

    /**
     * 会员ID
     */
    private Long id;

    /**
     * 排序
     */
    private Integer sort;

}
