package com.yiling.sjms.wash.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhigang.guo
 * @date 2023/3/1
 */
@Data
public class QueryFlowWashSupplyStockReportPageForm extends QueryPageListForm {

    @NotEmpty
    @ApiModelProperty(value = "月份 格式:yyyy-MM", example = "2023-02", required = true)
    @Pattern(regexp = "^\\d{4}-((0([1-9]))|(1(0|1|2)))$", message = "请填写正确的年月")
    private String soMonth;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String name;

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private Long crmId;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String provinceName;


    /**
     * 机构城市
     */
    @ApiModelProperty(value = "城市")
    private String cityName;

    @ApiModelProperty(value = "区县")
    private String regionName;

    /**
     * 机构业务省区
     */
    @ApiModelProperty(value = "机构业务省区")
    private String businessProvince;


    /**
     * 机构业务部门
     */
    @ApiModelProperty(value = "机构业务部门")
    private String businessDepartment;

    /**
     * 产品(sku)编码
     */
    @ApiModelProperty(value = "产品(sku)编码")
    private Long goodsCode;

    /**
     * 标准商品名称
     */
    @ApiModelProperty(value = "标准商品名称")
    private String goodsName;


    /**
     * 客户商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @ApiModelProperty(value = "商业级别")
    private Integer supplierLevel;

    @ApiModelProperty(value = "是否连锁总部")
    private Integer headChainFlag;


    /**
     * 商业属性 1-城市商业、2-县级商业
     */
    @ApiModelProperty(value = "商业属性")
    private Integer supplierAttribute;

    /**
     * 以岭级别
     */
    @ApiModelProperty(value = "以岭级别")
    private Integer ylLevel;


}
