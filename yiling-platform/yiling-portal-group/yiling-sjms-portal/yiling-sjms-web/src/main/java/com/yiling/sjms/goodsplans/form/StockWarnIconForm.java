package com.yiling.sjms.goodsplans.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 库存预警图标部分
 */
@Data
public class StockWarnIconForm extends QueryPageListForm {
    @ApiModelProperty(value = "经销商ID")
    private String eid;
    @ApiModelProperty(value = "经销商体系名称")
    private String enameGroup;
    @ApiModelProperty(value = "经销商体系级别名称")
    private String enameLevel;
    @ApiModelProperty(value = "商品规格ID")
    private Long specificationId;
    @ApiModelProperty(value = "排序:asc,desc")
    private String sort;
    @ApiModelProperty(value = "1-库存可销售天数,2-近3天日均销量,3-近30天日均销量,4-库存数量")
    private Integer iconTab;
    /**
     * 商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @ApiModelProperty(value = "商业级别")
    private Integer supplierLevel;
    /**
     * 商业体系
     */
    @ApiModelProperty(value = "商业体系")
    private Integer businessSystem;
}
