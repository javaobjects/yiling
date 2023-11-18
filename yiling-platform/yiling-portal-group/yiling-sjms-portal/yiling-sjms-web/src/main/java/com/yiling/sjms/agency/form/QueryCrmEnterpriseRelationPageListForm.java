package com.yiling.sjms.agency.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseRelationPageListForm extends QueryPageListForm {

    @ApiModelProperty(value = "业务部门")
    private String businessDepartment;

    @ApiModelProperty(value = "业务代表姓名")
    private String representativeName;

    @ApiModelProperty(value = "业务代表工号")
    private String representativeCode;

    @ApiModelProperty(value = "机构名称")
    private String customerName;

    @ApiModelProperty(value = "erp供应链角色：1-经销商 2-终端医院 3-终端药店")
    private Integer supplyChainRole;

    @ApiModelProperty(value = "最后操作时间-开始")
    private Date beginTime;

    @ApiModelProperty(value = "最后操作时间-结束")
    private Date endTime;

    @ApiModelProperty(value = "岗位编码")
    private Long postCode;

    @ApiModelProperty(value = "品种ID")
    private Long categoryId;

    @ApiModelProperty(value = "机构ID")
    private Long crmEnterpriseId;
}
