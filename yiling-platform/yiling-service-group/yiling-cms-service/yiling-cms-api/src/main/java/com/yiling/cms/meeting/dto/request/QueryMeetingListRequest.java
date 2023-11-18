package com.yiling.cms.meeting.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询会议管理列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMeetingListRequest extends BaseRequest {

    /**
     * 标题
     */
    private String title;

    /**
     * 引用业务线：1-2C患者 2-医生 3-患者 4-店员 5-医代
     */
    private List<Integer> useLineList;

    /**
     * 活动状态：1-未发布（未发布、未发布已过期） 2-已发布（进行中、已结束、未开始）
     */
    private Integer status;

}
