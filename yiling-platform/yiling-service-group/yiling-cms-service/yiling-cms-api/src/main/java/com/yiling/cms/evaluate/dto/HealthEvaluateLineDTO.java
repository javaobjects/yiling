package com.yiling.cms.evaluate.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 健康测评业务线
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateLineDTO extends BaseDTO {

    private static final long serialVersionUID = -7863296268309963238L;

    /**
     * cms_health_evaluate主键
     */
    private Long healthEvaluateId;

    /**
     * 引用业务线id
     */
    private Long lineId;

}
