package com.yiling.admin.erp.enterprise.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/4/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpSealedPageForm extends QueryPageListForm {

    /**
     * 商业ID
     */
    @ApiModelProperty(value = "商业ID")
    private Long eid;

    /**
     * 商业名称
     */
    @ApiModelProperty(value = "商业名称")
    private String ename;

    /**
     * 流向类型，字典(erp_flow_type)：1-采购流向 2-销售流向 0-全部
     */
    @ApiModelProperty(value = "流向类型，字典(erp_flow_type)：1-采购流向 2-销售流向 0-全部")
    private Integer type;

    /**
     * 封存状态，字典(erp_flow_sealed_status)：1-已解封 2-已封存 0-全部
     */
    @ApiModelProperty(value = "封存状态，字典(erp_flow_sealed_status)：1-已解封 2-已封存 0-全部")
    private Integer status;

}
