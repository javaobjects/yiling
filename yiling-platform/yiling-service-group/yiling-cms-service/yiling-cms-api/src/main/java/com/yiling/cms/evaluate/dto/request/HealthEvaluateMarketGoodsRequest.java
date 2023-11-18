package com.yiling.cms.evaluate.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 健康测评营销商品
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateMarketGoodsRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    private Long id;

    /**
     * cms_health_evaluate主键
     */
    private Long healthEvaluateId;

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 结果排序
     */
    private Integer resultRank;

    /**
     * 适应症
     */
    private String indications;

    /**
     * 跳转链接
     */
    private String jumpUrl;

}
