package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 切换QA显示状态
 *
 * @author: fan.shen
 * @date: 2023/3/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SwitchQAStatusRequest extends BaseRequest {

    private static final long serialVersionUID = 8387299024010863751L;

    /**
     * 问答id
     */
    private Long id;

    /**
     * 展示状态 1-展示，2-关闭
     */
    private Integer showStatus;
}