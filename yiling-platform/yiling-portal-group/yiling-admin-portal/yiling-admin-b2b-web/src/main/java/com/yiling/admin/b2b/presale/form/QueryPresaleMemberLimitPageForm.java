package com.yiling.admin.b2b.presale.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPresaleMemberLimitPageForm extends QueryPageListForm {

    @ApiModelProperty("营销活动id")
    @NotNull(message = "未选中策略满赠活动")
    private Long marketingPresaleId;
}
