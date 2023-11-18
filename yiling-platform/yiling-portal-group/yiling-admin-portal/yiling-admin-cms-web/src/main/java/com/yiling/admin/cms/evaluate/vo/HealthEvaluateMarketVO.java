package com.yiling.admin.cms.evaluate.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 健康测评营销
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-08
 */
@Data
@Accessors(chain = true)
public class HealthEvaluateMarketVO {

    /**
     * 关联商品
     */
    @ApiModelProperty(value = "关联商品")
    private List<HealthEvaluateMarketGoodsVO> goodsList;

    /**
     * 改善建议
     */
    @ApiModelProperty(value = "改善建议")
    private List<HealthEvaluateMarketAdviceVO> adviceList;

    /**
     * 推广服务
     */
    @ApiModelProperty(value = "推广服务")
    private List<HealthEvaluateMarketPromoteVO> promoteList;

}
