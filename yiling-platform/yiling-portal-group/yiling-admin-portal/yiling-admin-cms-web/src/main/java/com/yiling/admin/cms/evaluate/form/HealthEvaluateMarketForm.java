package com.yiling.admin.cms.evaluate.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
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
 * @date 2022-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HealthEvaluateMarketForm extends BaseForm {

    @ApiModelProperty(value = "关联商品")
    private List<HealthEvaluateMarketGoodsForm> goodsList;

    @ApiModelProperty(value = "改善建议")
    private List<HealthEvaluateMarketAdviceForm> adviceList;

    @ApiModelProperty(value = "推广服务")
    private List<HealthEvaluateMarketPromoteForm> promoteList;

}
