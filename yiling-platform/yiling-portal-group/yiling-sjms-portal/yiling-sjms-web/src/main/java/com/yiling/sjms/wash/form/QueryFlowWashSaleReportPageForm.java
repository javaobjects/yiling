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
public class QueryFlowWashSaleReportPageForm extends QueryPageListForm {


    @NotEmpty
    @ApiModelProperty(value = "月份 格式:yyyy-MM", example = "2023-02", required = true)
    @Pattern(regexp = "^\\d{4}-((0([1-9]))|(1(0|1|2)))$", message = "请填写正确的年月")
    private String soMonth;


    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码,用户模糊搜索经销商后,带过来经销商编码")
    private Long crmId;

    /**
     * 商业名称（商家名称）
     */
    @ApiModelProperty(value = "商业名称（商家名称）")
    private String ename;

    /**
     * 原始客户名称
     */
    @ApiModelProperty(value = "原始客户名称")
    private String originalEnterpriseName;

    /**
     * 原始商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String soGoodsName;

    /**
     * 原始商品规格
     */
    @ApiModelProperty(value = "原始商品规格")
    private String soSpecifications;

    /**
     * 通过商品编码搜索
     */
    @ApiModelProperty(value = "通过商品编码搜索")
    private Long goodsCode;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    private String department;

    /**
     * 业务部门
     */
    @ApiModelProperty(value = "业务部门")
    private String businessDepartment;

    /**
     * 省区
     */
    @ApiModelProperty(value = "省区")
    private String provincialArea;

    /**
     * 业务省区
     */
    @ApiModelProperty(value = "业务省区")
    private String businessProvince;

    /**
     * 区办
     */
    @ApiModelProperty(value = "区办")
    private String regionName;

    /**
     * 业务代表
     */
    @ApiModelProperty(value = "业务代表")
    private String representativeCode;
}
