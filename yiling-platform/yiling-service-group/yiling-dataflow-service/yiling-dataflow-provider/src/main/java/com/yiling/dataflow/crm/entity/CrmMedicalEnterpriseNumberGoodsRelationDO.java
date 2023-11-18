package com.yiling.dataflow.crm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_medical_enterprise_number_goods_relation")
public class CrmMedicalEnterpriseNumberGoodsRelationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 年份
     */
    private String year;

    /**
     * 月份
     */
    private String month;

    /**
     * 板块名称
     */
    private String plateName;

    /**
     * 板块省区
     */
    private String plateProvinceArea;

    /**
     * 部门
     */
    private String department;

    /**
     * 业务部门
     */
    private String businessDepartment;

    /**
     * 业务省区
     */
    private String departmentProvinceArea;

    /**
     * 片区
     */
    private String areaName;

    /**
     * 业务区域代码
     */
    private String businessAreaCode;

    /**
     * 业务区域
     */
    private String businessAreaName;

    /**
     * 业务区域描述
     */
    private String businessAreaDescription;

    /**
     * 上级主管编码
     */
    private String superiorSupervisorCode;

    /**
     * 上级主管名称
     */
    private String superiorSupervisorName;

    /**
     * 主管编码
     */
    private String managerCode;

    /**
     * 主管名称
     */
    private String managerName;

    /**
     * 代表编码
     */
    private String representativeCode;

    /**
     * 代表名称
     */
    private String representativeName;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 省区
     */
    private String provinceCode;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县代码
     */
    private String districtCountyCode;

    /**
     * 区县
     */
    private String districtCounty;

    /**
     * 产品组
     */
    private String productGroup;

    /**
     * 医院类型
     */
    private String medicalType;

    /**
     * 以岭级别
     */
    private String medicalYilingType;

    /**
     * 管理级别
     */
    private String medicalManagerType;

    /**
     * 国家级别
     */
    private String medicalLevel;

    /**
     * 级别(七级)
     */
    private String medicalSevenType;


}
