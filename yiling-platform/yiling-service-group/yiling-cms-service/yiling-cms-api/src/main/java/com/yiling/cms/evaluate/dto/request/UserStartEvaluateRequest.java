package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 用户开始测评
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserStartEvaluateRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * cms_health_evaluate主键
     */
    private Long healthEvaluateId;

}
