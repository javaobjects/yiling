package com.yiling.admin.b2b.strategy.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

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
public class DeleteStrategyBuyerLimitForm extends BaseForm {

    @ApiModelProperty("营销活动id")
    @NotNull(message = "未选中策略满赠活动")
    private Long marketingStrategyId;

    @ApiModelProperty("企业id-单独删除时使用")
    private Long eid;

    @ApiModelProperty("企业id集合-批量删除时使用")
    private List<Long> eidList;

    @ApiModelProperty("企业ID-精确搜索-删除搜索结果时使用")
    private Long eidPage;

    @ApiModelProperty("企业名称-模糊搜索-删除搜索结果时使用")
    private String enamePage;
}
