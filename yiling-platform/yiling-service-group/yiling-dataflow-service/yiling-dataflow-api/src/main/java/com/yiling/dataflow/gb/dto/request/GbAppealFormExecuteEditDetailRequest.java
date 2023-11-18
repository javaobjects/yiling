package com.yiling.dataflow.gb.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbAppealFormExecuteEditDetailRequest extends BaseRequest {

    /**
     * 团购处理列表ID
     */
    private Long appealFormId;

    /**
     * 扣减/增加数据ID
     */
    private Long appealAllocationId;

    /**
     * 需要一起修改的ID（编辑处理结果明细时，仅同步修改对应扣减/新增的数量）
     */
    private Long negateAllocationId;

    /**
     * 扣减/增加数量
     */
    private BigDecimal quantity;

    /**
     * 处理类型：1-自动 2-人工
     *
     * {@link com.yiling.dataflow.gb.enums.GbExecTypeEnum}
     */
    private Integer execType;

    /**
     * 机构部门ID(用户自己填写部门名称时此id为0)
     */
    private Long orgId;

    /**
     * 机构部门
     */
    private String department;

    /**
     * 机构业务部门ID(用户自己填写业务部门名称时此id为0)
     */
    private Long businessOrgId;

    /**
     * 业务部门
     */
    private String businessDepartment;

    /**
     * 业务代表工号
     */
    private String representativeCode;

    /**
     * 代表姓名
     */
    private String representativeName;

    /**
     * 岗位代码
     */
    private Long postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 省区
     */
    private String provincialArea;

    /**
     * 业务省区
     */
    private String businessProvince;

    /**
     * 机构区办代码
     */
    private String districtCountyCode;

    /**
     * 业务区域（机构区办）
     */
    private String districtCounty;

    /**
     * 上级主管工号
     */
    private String superiorSupervisorCode;

    /**
     * 上级主管姓名
     */
    private String superiorSupervisorName;

}
