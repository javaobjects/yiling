package com.yiling.export.export.bo;

import lombok.Data;

/**
 * 三者关系合并版EXCEL模板
 *
 * @author: yong.zhang
 * @date: 2023/2/18 0018
 */
@Data
public class ExportCrmEnterpriseRelationShipBO {

    /**
     * 部门
     */
    private String department;
    /**
     * 业务部门
     */
    private String businessDepartment;
    /**
     * 省区
     */
    private String provincialArea;
    /**
     * 业务省区
     */
    private String businessProvince;
    /**
     * 业务区域
     */
    private String businessArea;
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
     * 客户编码
     */
    private String customerCode;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 供应链角色
     */
    private String supplyChainRole;
    /**
     * 省份
     */
    private String provinceName;
    /**
     * 城市
     */
    private String cityName;
    /**
     * 区县
     */
    private String regionName;
    /**
     * 产品组
     */
    private String productGroup;
    /**
     * 辖区ID
     */
    private Long manorId;
    /**
     * 品类ID
     */
    private Long categoryId;
    private String manorName;
    /**
     * 品类ID
     */
    private String categoryName;

    /**
     * 是否代跑
     */
    private Integer substituteRunning;
    private String substituteRunningDesc;

}
