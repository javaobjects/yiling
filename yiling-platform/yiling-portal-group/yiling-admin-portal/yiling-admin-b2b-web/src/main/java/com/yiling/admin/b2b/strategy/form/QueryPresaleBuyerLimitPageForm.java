package com.yiling.admin.b2b.strategy.form;

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
public class QueryPresaleBuyerLimitPageForm extends QueryPageListForm {

    @ApiModelProperty("营销活动id")
    private Long marketingPresaleId;

    @ApiModelProperty("企业ID-精确搜索")
    private Long eid;

    @ApiModelProperty("企业名称-模糊搜索")
    private String ename;
}
