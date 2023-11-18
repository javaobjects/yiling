package com.yiling.f2b.admin.enterprise.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业采购关系 Form
 *
 * @author: yuecheng.chen
 * @date: 2021/6/7 0007
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPurchaseRelationPageListForm extends QueryPageListForm {

    /**
     * 采购商ID
     */
    @ApiModelProperty("采购商ID")
    private Long buyerEid;

    /**
     * 供应商渠道ID
     */
    @ApiModelProperty("供应商渠道ID")
    private Long sellerChannelId;

}
