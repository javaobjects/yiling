package com.yiling.hmc.meeting.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 获取会议签到信息
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitCheckCodeRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    private Long id;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 电话
     */
    private String mobile;


}
