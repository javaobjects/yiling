package com.yiling.sjms.gb.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/23
 */
@Data
public class QueryGbAllocationListPageForm extends QueryPageListForm {

    /**
     * 所属年月
     */
    @ApiModelProperty(value = "所属年月")
    private String matchMonth;

    /**
     * 团购编号
     */
    @ApiModelProperty(value = "团购编号")
    private String gbNo;

    /**
     * 团购月份
     */
    @ApiModelProperty(value = "团购月份")
    private String gbMonth;

    /**
     * 商业编码
     */
    @ApiModelProperty(value = "商业编码")
    private Long crmId;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String enterpriseName;

    /**
     * 标准产品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private Long goodsCode;

}
