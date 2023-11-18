package com.yiling.sjms.agency.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/23 0023
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgencyFormPageForm extends QueryPageListForm {

    /**
     * form表主键
     */
    @ApiModelProperty(value = "formId")
    private Long formId;

    /**
     * 1-团购单据 2-机构新增 3-机构修改 4-机构锁定 5-机构解锁 6-机构扩展信息修改 7-机构三者关系变更
     */
    @ApiModelProperty("1-团购单据 2-机构新增 3-机构修改 4-机构锁定 5-机构解锁 6-机构扩展信息修改 7-机构三者关系变更")
    private Integer formType;
}