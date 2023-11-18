package com.yiling.b2b.app.member.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B移动端-查询省钱计算器订单分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-11-14
 */
@Data
@ApiModel
public class QueryFrugalPageForm extends QueryPageListForm {

    /**
     * 会员ID
     */
    @NotNull
    @ApiModelProperty(value = "会员ID",required = true)
    private Long memberId;

}
