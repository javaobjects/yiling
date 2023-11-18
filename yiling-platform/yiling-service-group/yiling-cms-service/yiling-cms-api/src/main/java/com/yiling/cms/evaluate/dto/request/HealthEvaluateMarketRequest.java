package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 健康测评营销
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateMarketRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 改善建议
     */
    private List<HealthEvaluateMarketAdviceRequest> adviceList;

    /**
     * 关联药品
     */
    private List<HealthEvaluateMarketGoodsRequest> goodsList;

    /**
     * 推广服务
     */
    private List<HealthEvaluateMarketPromoteRequest> promoteList;
}
