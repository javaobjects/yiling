package com.yiling.sjms.crm.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryBakGoodsInfoPageForm
 * @描述
 * @创建时间 2023/6/25
 * @修改人 shichen
 * @修改时间 2023/6/25
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBakGoodsInfoPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "数据日期（备份表日期）")
    private Date dataDate;

    /**
     * crm商品编码
     */
    @ApiModelProperty(value = "crm商品编码")
    private Long goodsCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品状态，不传：所有状态 0:有效，1：失效")
    private Integer status;
}
