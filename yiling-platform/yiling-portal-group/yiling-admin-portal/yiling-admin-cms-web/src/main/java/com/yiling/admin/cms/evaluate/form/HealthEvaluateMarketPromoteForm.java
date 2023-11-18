package com.yiling.admin.cms.evaluate.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 健康测评推广
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateMarketPromoteForm extends BaseForm {

    private Long id;

    /**
     * cms_health_evaluate主键
     */
    @ApiModelProperty(value = "cms_health_evaluate主键")
    private Long healthEvaluateId;

    /**
     * 跳转链接
     */
    @ApiModelProperty(value = "跳转链接")
    private String jumpUrl;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String pic;
}
