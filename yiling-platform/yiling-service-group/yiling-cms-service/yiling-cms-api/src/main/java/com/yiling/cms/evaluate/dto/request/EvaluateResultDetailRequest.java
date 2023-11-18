package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 健康测评题目
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EvaluateResultDetailRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 测评题目id
     */
    private Long healthEvaluateQuestionId;

    /**
     * 单/多选 id 列表
     */
    private List<Long> selectIdList;

    /**
     * 身高
     */
    private BigDecimal height;

    /**
     * 体重
     */
    private BigDecimal weight;
}
