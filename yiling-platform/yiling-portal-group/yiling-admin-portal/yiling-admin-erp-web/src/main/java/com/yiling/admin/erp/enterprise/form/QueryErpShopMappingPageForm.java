package com.yiling.admin.erp.enterprise.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryErpShopMappingPageForm
 * @描述
 * @创建时间 2023/3/21
 * @修改人 shichen
 * @修改时间 2023/3/21
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpShopMappingPageForm extends QueryPageListForm {

    /**
     * 总店企业id
     */
    @ApiModelProperty(value = "总店企业id")
    private Long mainShopEid;

    /**
     * 门店企业id
     */
    @ApiModelProperty(value = "门店企业id")
    private Long shopEid;
}
