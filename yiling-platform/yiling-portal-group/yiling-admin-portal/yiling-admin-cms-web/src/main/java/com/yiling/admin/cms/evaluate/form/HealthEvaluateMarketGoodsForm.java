package com.yiling.admin.cms.evaluate.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 健康测评营销商品
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateMarketGoodsForm extends BaseForm {

    private Long id;

    /**
     * cms_health_evaluate主键
     */
    @ApiModelProperty(value = "cms_health_evaluate主键")
    private Long healthEvaluateId;

    /**
     * 标准库ID
     */
    @ApiModelProperty(value = "标准库ID")
    private Long standardId;

    /**
     * 结果排序
     */
    @ApiModelProperty(value = "结果排序")
    private Integer resultRank;

    /**
     * 适应症
     */
    @ApiModelProperty(value = "适应症")
    private String indications;

    /**
     * 跳转链接
     */
    @ApiModelProperty(value = "跳转链接")
    private String jumpUrl;
}
