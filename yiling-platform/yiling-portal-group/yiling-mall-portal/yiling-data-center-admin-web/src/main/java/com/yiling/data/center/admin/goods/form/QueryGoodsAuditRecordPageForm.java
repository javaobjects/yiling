package com.yiling.data.center.admin.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsAuditRecordPageForm extends QueryPageListForm {


    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id", example = "1")
    private Long goodsId;

}
