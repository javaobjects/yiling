package com.yiling.b2b.app.enterprise.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业采购申请分页列表 Form
 *
 * @author: lun.yu
 * @date: 2022/01/18
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterprisePurchaseApplyPageForm extends QueryPageListForm {

    /**
     * 企业名称（全模糊查询）
     */
    @Length(max = 50)
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 审核状态：1-待审核 2-已建采 3-已驳回
     */
    @NotNull
    @ApiModelProperty("审核状态：1-待审核 2-已建采 3-已驳回")
    private Integer authStatus;

}
