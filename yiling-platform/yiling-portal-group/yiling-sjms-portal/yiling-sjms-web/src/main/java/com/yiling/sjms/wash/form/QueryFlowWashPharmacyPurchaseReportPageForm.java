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
public class QueryFlowWashPharmacyPurchaseReportPageForm extends QueryPageListForm {

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
     * 机构省份
     */
    @ApiModelProperty(value = "机构省份")
    private String provinceName;

    /**
     * 机构城市
     */
    @ApiModelProperty(value = "机构城市")
    private String cityName;

    @ApiModelProperty(value = "机构区县")
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
     * 药店级别 1-A级；2-B级；3-C级
     */
    @ApiModelProperty(value = "药店级别")
    private Integer pharmacyLevel;

    /**
     * 药店属性 1-连锁分店；2-单体药店
     */
    @ApiModelProperty(value = "药店属性")
    private Integer pharmacyAttribute;

    /**
     * 药店类型 1-直营；2-加盟
     */
    @ApiModelProperty(value = "药店类型")
    private Integer pharmacyType;

}
