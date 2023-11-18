package com.yiling.sjms.sale.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: GXL
 * @date: 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaleDepartmentSubTargetResolveDetailVO extends BaseVO {


    /**
     * 指标ID
     */
    @ApiModelProperty(value = "指标id")
    private Long saleTargetId;
    /**
     * 指标名称
     */
    @ApiModelProperty(value = "指标名称")
    private String name;

    /**
     * 指标编号
     */
    @ApiModelProperty(value = "指标编号")
    private String targetNo;
    /**
     * 指标年份
     */
    @ApiModelProperty(value = "指标年份")
    private Long targetYear;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "指标id")
    private Long departId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "事业部名称")
    private String departName;

    /**
     * 标签为省区的部门ID
     */
    private Long departProvinceId;

    /**
     * 标签为省区的部门名称
     */
    @ApiModelProperty(value = "指标id")
    private String departProvinceName;

    /**
     * 标签为区办的部门ID
     */
    @ApiModelProperty(value = "区办编码")
    private Long departRegionId;

    /**
     * 标签为区办的部门名称
     */
    @ApiModelProperty(value = "区办")
    private String departRegionName;

    /**
     * 品类ID
     */
    private Long categoryId;

    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品种名称")
    private String categoryName;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "产品编码")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String goodsName;

    /**
     * 产品组id
     */
    private Long productGroupId;


    /**
     * 上级主管编码
     */
    @ApiModelProperty(value = "上级主管工号")
    private String superiorSupervisorCode;

    /**
     * 上级主管名称
     */
    @ApiModelProperty(value = "主管姓名")
    private String superiorSupervisorName;

    /**
     * 代表编码
     */
    @ApiModelProperty(value = "代表工号")
    private String representativeCode;

    /**
     * 代表名称
     */
    @ApiModelProperty(value = "代表姓名")
    private String representativeName;

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 供应链类型：1商业公司 2医疗机构 3零售机构
     */
    @ApiModelProperty(value = "供应链类型：字典crm_supply_chain_role")
    private Integer supplyChainRole;

    /**
     * 岗位代码
     */
    @ApiModelProperty(value = "岗位代码")
    private Long postCode;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postName;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String city;

    /**
     * 区县
     */
    @ApiModelProperty(value = "区县")
    private String districtCounty;
    /**
     * 月份目标值分解状态 1未分解（任意月份没填） 2已分解
     */
    @ApiModelProperty(value = "1未分解（任意月份没填） 2已分解")
    private Integer resolveStatus;

    /**
     * 1月份目标
     */
    private BigDecimal targetJan;

    /**
     * 2月份目标
     */
    private BigDecimal targetFeb;

    /**
     * 3月份目标
     */
    private BigDecimal targetMar;

    /**
     * 4月份目标
     */
    private BigDecimal targetApr;

    /**
     * 5月份目标
     */
    private BigDecimal targetMay;

    /**
     * 6月份目标
     */
    private BigDecimal targetJun;

    /**
     * 7月份目标
     */
    private BigDecimal targetJul;

    /**
     * 8月份目标
     */
    private BigDecimal targetAug;

    /**
     * 9月份目标
     */
    private BigDecimal targetSep;

    /**
     * 10月份目标
     */
    private BigDecimal targetOct;

    /**
     * 11月份目标
     */
    private BigDecimal targetNov;



    /**
     * 12月份目标
     */
    private BigDecimal targetDec;



}
