package com.yiling.sjms.agency.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.sjms.agency.dto.request.SaveAgencyRelationShipChangeRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 机构新增修改表单请求参数
 *
 * @author: yong.zhang
 * @date: 2023/2/22 0022
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class  SaveAgencyRelationShipChangeFormForm extends BaseForm {

    /**
     * 流程form表id
     */
    private Long formId;

    /**
     * 主键
     */
    private Long id;

    /**
     * 调整原因
     */
    @ApiModelProperty("调整原因")
    private String adjustReason;

    /**
     * 机构基本信息id
     */
    private Long crmEnterpriseId;

    /**
     * crm系统对应客户名称
     */
    @ApiModelProperty("选择机构名称")
    private String name;

    /**
     * 社会信用统一代码
     */
    @ApiModelProperty("社会信用统一代码")
    private String licenseNumber;

    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店
     */
    @ApiModelProperty("erp供应链角色：1-经销商 2-终端医院 3-终端药店")
    private Integer supplyChainRole;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    @ApiModelProperty("所属城市名称")
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    @ApiModelProperty("所属区域名称")
    private String regionName;

    /**
     * 岗位代码
     */
    @ApiModelProperty("岗位代码")
    private Long postCode;

    /**
     * 岗位名称
     */
    @ApiModelProperty("岗位名称")
    private String postName;

    /**
     * 代表编码
     */
    @ApiModelProperty("代表编码")
    private String representativeCode;

    /**
     * 代表名称
     */
    @ApiModelProperty("代表名称")
    private String representativeName;

    /**
     * 部门
     */
    @ApiModelProperty("部门")
    private String department;

    /**
     * 业务部门
     */
    @ApiModelProperty("业务部门")
    private String businessDepartment;

    /**
     * 省区
     */
    @ApiModelProperty("省区")
    private String provincialArea;

    /**
     * 业务省区
     */
    private String businessProvince;

    /**
     * 业务区域代码
     */
    @ApiModelProperty("业务区域代码")
    private String businessAreaCode;

    /**
     * 业务区域
     */
    @ApiModelProperty("业务区域")
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
     * 三者关系总数量
     */
    @ApiModelProperty("三者关系总数量")
    private Integer relationTotal;

    /**
     * 三者关系变更数量
     */
    @ApiModelProperty("三者关系变更数量")
    private Integer changeRelationTotal;

    @ApiModelProperty("备注")
    private String remark;

    private List<SaveAgencyRelationShipChangeForm> relationShip;
}