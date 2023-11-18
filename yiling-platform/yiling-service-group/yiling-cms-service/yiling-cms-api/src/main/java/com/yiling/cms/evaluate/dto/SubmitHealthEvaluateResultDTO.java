package com.yiling.cms.evaluate.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 健康测评结果
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitHealthEvaluateResultDTO extends BaseDTO {

    private static final long serialVersionUID = -7863296268309963238L;

    /**
     * 测评结果
     */
    private String evaluateResult;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 健康小贴士
     */
    private String healthTip;

    /**
     * 改善建议
     */
    private List<HealthEvaluateMarketAdviceDTO> marketAdviceList;

    /**
     * 关联药品
     */
    private List<HealthEvaluateMarketGoodsDTO> marketGoodsList;

}
