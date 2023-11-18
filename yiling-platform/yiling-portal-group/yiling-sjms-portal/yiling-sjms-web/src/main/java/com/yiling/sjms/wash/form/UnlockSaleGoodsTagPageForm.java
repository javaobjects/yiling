package com.yiling.sjms.wash.form;

import com.yiling.sjms.crm.form.QueryCrmGoodsTagPageForm;
import com.yiling.sjms.crm.form.QueryGoodsInfoPageForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryGoodsInfoPageForm
 * @描述
 * @创建时间 2023/4/10
 * @修改人 shichen
 * @修改时间 2023/4/10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockSaleGoodsTagPageForm extends QueryCrmGoodsTagPageForm {
    @ApiModelProperty(value = "销量规则Id")
    private Long ruleId;
}
