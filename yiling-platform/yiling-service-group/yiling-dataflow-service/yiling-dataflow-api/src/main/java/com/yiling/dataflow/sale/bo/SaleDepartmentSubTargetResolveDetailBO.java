package com.yiling.dataflow.sale.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class SaleDepartmentSubTargetResolveDetailBO extends BaseDTO {


    private static final long serialVersionUID = 3374049243811063250L;
    /**
     * 指标ID
     */
    private Long saleTargetId;
    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标编号
     */
    private String targetNo;
    /**
     * 指标年份
     */
    private Long targetYear;

    /**
     * 部门ID
     */
    private Long departId;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 标签为省区的部门ID
     */
    private Long departProvinceId;

    /**
     * 标签为省区的部门名称
     */
    private String departProvinceName;

    /**
     * 标签为区办的部门ID
     */
    private Long departRegionId;

    /**
     * 标签为区办的部门名称
     */
    private String departRegionName;

    /**
     * 品类ID
     */
    private Long categoryId;

    /**
     * 品类名称
     */
    private String categoryName;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 产品组id
     */
    private Long productGroupId;

    /**
     * 产品组名称
     */
    private String productGroupName;

    /**
     * 上级主管编码
     */
    private String superiorSupervisorCode;

    /**
     * 上级主管名称
     */
    private String superiorSupervisorName;

    /**
     * 代表编码
     */
    private String representativeCode;

    /**
     * 代表名称
     */
    private String representativeName;

    /**
     * 机构编码
     */
    private String customerCode;

    /**
     * 机构名称
     */
    private String customerName;

    /**
     * 供应链类型：1商业公司 2医疗机构 3零售机构
     */
    private Integer supplyChainRole;

    /**
     * 岗位代码
     */
    private Long postCode;

    /**
     * 岗位名称
     */
    private String postName;

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
     * 月份目标值分解状态 1未分解（任意月份没填） 2已分解
     */
    private Integer resolveStatus;

    /**
     * 12月份目标
     */
    private BigDecimal targetDec;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 本年度增加单位元
     */
    private BigDecimal currentIncrease;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String districtCounty;
}
