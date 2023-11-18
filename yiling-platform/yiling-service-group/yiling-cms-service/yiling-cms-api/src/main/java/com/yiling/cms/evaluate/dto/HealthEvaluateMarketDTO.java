package com.yiling.cms.evaluate.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 健康测评
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@Accessors(chain = true)
public class HealthEvaluateMarketDTO implements java.io.Serializable {

    private static final long serialVersionUID = -7863296268309963238L;

    /**
     * 关联商品
     */
    private List<HealthEvaluateMarketGoodsDTO> goodsList;

    /**
     * 改善建议
     */
    private List<HealthEvaluateMarketAdviceDTO> adviceList;

    /**
     * 推广服务
     */
    private List<HealthEvaluateMarketPromoteDTO> promoteList;

}
