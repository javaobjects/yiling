package com.yiling.sjms.goodsplans.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 *
 */
@Data
public class FlowAnalyseGoodsForm  extends QueryPageListForm {
    @ApiModelProperty("商品名称")
    private String goodsName;
}
