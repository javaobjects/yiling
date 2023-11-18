package com.yiling.admin.b2b.goods.vo;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseGoodsPageListForm extends QueryPageListForm {

    /**
     * 企业名称（全模糊查询）
     */
    @ApiModelProperty(value = "企业名称")
    private String name;
}
