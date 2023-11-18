package com.yiling.b2b.admin.promotion.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.marketing.common.enums.PromotionErrorCode;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSaveForm extends BaseForm {

    @ApiModelProperty(value = "促销活动id")
    private Long id;

    @ApiModelProperty(value = "促销活动信息")
    private PromotionActivitySaveForm activity;

    @ApiModelProperty(value = "促销活动企业")
    private List<PromotionEnterpriseLimitSaveForm> enterpriseLimitList;

    @ApiModelProperty(value = "促销商品")
    private List<PromotionGoodsLimitSaveForm> goodsLimitList;

    @ApiModelProperty(value = "赠品名称")
    private String goodsGiftName;

    /**
     * 参数校验
     */
    public void check() {
        // 1、校验销售渠道
        if (CollUtil.isEmpty(this.activity.getPlatformSelected())) {
            throw new BusinessException(PromotionErrorCode.PLATFORM_SELECTED);
        }
    }
}
