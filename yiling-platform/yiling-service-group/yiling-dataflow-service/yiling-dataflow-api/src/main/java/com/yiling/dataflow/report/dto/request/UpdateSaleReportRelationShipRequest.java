package com.yiling.dataflow.report.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/3/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateSaleReportRelationShipRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 流向主表Id
     */
    private Long Id;

    /**
     * 机构部门
     */
    private String department;

    /**
     * 机构业务部门
     */
    private String businessDepartment;

    /**
     * 机构业务省区
     */
    private String businessProvince;

    /**
     * 主管工号
     */
    private String superiorSupervisorCode;

    /**
     * 主管名称
     */
    private String superiorSupervisorName;

    /**
     * 代表工号
     */
    private String representativeCode;

    /**
     * 代表姓名
     */
    private String representativeName;

    /**
     * 岗位编码
     */
    private Long postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 业务区域
     */
    private String businessArea;


}
