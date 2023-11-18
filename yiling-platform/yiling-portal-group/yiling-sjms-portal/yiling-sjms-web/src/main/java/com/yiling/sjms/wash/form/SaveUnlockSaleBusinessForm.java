package com.yiling.sjms.wash.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveUnlockSaleBusinessForm extends BaseForm {

    @ApiModelProperty("规则ID")
    private Long ruleId;

    @ApiModelProperty("商业公司ID集合")
    private List<Long> crmEnterpriseIds;

}
