package com.yiling.sjms.crm.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryHospitalDrugstoreRelationPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "院外药店机构id")
    private Long drugstoreOrgId;

    @ApiModelProperty(value = "医疗机构id")
    private Long hospitalOrgId;

    @ApiModelProperty(value = "品种id")
    private Long categoryId;

    @ApiModelProperty(value = "品种名称")
    private String categoryName;

    @ApiModelProperty(value = "产品code")
    private Long crmGoodsCode;

    @ApiModelProperty(value = "最后操作开始时间")
    private Date startOpTime;

    @ApiModelProperty(value = "最后操作结束时间")
    private Date endOpTime;

    @ApiModelProperty(value = "状态 字典 hospital_drugstore_rel_status")
    private Integer status;
}
