package com.yiling.admin.data.center.report.form;



import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * B2B获取标准库商品标签
 * @author: wei.wang
 * @date: 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryB2BOrderGoodsLagsPageForm extends QueryPageListForm {
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
}
