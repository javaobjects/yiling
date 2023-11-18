package com.yiling.f2b.web.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEsGoodsSearchForm extends QueryPageListForm {
    /**
     * 搜索词
     */
    @ApiModelProperty(value = "搜索词")
    private String key;

    /**
     * 企业ID
     */
    private List<Long> eids;
}
