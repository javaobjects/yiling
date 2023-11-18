package com.yiling.sjms.wash.form;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.sjms.wash.vo.LocationUnlockCustomerTreeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockSaleCustomerRangeForm extends BaseForm {

    @ApiModelProperty(value = "客户分类集合")
    private List<Integer> classificationIds;

    @ApiModelProperty(value = "关键词集合")
    private String keywords;

}
