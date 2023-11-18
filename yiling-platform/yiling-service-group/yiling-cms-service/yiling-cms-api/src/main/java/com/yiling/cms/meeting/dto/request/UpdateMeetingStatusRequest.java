package com.yiling.cms.meeting.dto.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新会议状态 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMeetingStatusRequest extends BaseRequest {

    /**
     * ID
     */
    @NotNull
    private Long id;

    /**
     * 操作类型：1-发布 2-取消发布 3-删除
     */
    @NotNull
    @Range(min = 1, max = 3)
    private Integer opType;

}
