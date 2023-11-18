package com.yiling.f2b.admin.enterprise.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询渠道商可供采购企业 Form
 *
 * @author: yuecheng.chen
 * @date: 2021/6/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCanPurchaseEnterprisePageListForm extends QueryPageListForm {

    /**
     * 采购企业ID
     */
    @ApiModelProperty("采购企业ID")
    private Long buyerEid;

    /**
     * 采购渠道商ID
     */
    @ApiModelProperty("采购渠道商ID")
    private Long buyerChannelId;

    /**
     * 销售渠道商ID
     */
    @ApiModelProperty("销售渠道商ID")
    private Long sellerChannelId;

    /**
     * 渠道商名称
     */
    @ApiModelProperty("渠道商名称")
    private String sellerName;
}
