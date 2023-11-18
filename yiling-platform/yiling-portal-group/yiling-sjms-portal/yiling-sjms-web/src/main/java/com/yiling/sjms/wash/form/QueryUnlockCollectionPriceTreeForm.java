package com.yiling.sjms.wash.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUnlockCollectionPriceTreeForm extends BaseForm {

    @ApiModelProperty(value = "采集明细id")
    private Long unlockCollectionDetailId;

    @ApiModelProperty(value = "标准产品编码")
    private Long crmGoodsCode;


}
