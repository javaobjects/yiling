package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCrmEnterpriseRelationShipPinchRunnerRequest extends BaseRequest {

    /**
     * 销量计入主管岗位代码
     */
    private Long businessSuperiorPostCode;

    /**
     * 销量计入主管岗位名称
     */
    private String businessSuperiorPostName;

    /**
     * 销量计入主管工号
     */
    private String businessSuperiorCode;

    /**
     * 销量计入主管姓名
     */
    private String businessSuperiorName;

    /**
     * 销量计入部门
     */
    private String superiorDepartment;

    /**
     * 销量计入省区
     */
    private String superiorProvincial;

    /**
     * 销量计入业务部门
     */
    private String businessSuperiorDepartment;

    /**
     * 销量计入业务省区
     */
    private String businessSuperiorProvince;

    /**
     * 销量计入业务区域代码
     */
    private String businessSuperiorAreaCode;

    /**
     * 销量计入业务区域
     */
    private String businessSuperiorArea;

    /**
     * 省区经理岗位代码
     */
    private Long provincialManagerPostCode;

    /**
     * 省区经理岗位名称
     */
    private String provincialManagerPostName;

    /**
     * 省区经理工号
     */
    private String provincialManagerCode;

    /**
     * 省区经理姓名
     */
    private String provincialManagerName;

}
