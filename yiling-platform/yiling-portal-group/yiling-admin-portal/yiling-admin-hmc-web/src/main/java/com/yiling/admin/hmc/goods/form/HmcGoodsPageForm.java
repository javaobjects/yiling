package com.yiling.admin.hmc.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 HmcGoodsPageRequest
 * @描述
 * @创建时间 2022/3/30
 * @修改人 shichen
 * @修改时间 2022/3/30
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HmcGoodsPageForm extends QueryPageListForm {

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("保险公司id")
    private Long insuranceCompanyId;
}
