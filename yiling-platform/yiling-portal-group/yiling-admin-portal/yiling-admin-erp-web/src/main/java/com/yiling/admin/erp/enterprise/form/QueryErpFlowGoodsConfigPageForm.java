package com.yiling.admin.erp.enterprise.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "查询ERP流向非以岭商品配置信息分页参数")
public class QueryErpFlowGoodsConfigPageForm extends QueryPageListForm {

    /**
     * 商业ID
     */
    @ApiModelProperty(value = "商业ID")
    private Long eid;

    /**
     * 商业名称
     */
    @ApiModelProperty(value = "商业名称")
    private String ename;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品内码（供应商的ERP的商品主键）
     */
    @ApiModelProperty(value = "商品内码")
    private String goodsInSn;

    /**
     * 创建时间-开始
     */
    @ApiModelProperty(value = "创建时间-开始")
    private String startCreateTime;

    /**
     * 创建时间-结束
     */
    @ApiModelProperty(value = "创建时间-结束")
    private String endCreateTime;

}
