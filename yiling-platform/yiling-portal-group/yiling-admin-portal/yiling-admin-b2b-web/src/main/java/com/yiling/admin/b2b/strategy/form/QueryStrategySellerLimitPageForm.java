package com.yiling.admin.b2b.strategy.form;

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
public class QueryStrategySellerLimitPageForm extends QueryPageListForm {

    @ApiModelProperty("营销活动id")
    @NotNull(message = "未选中策略满赠活动")
    private Long marketingStrategyId;

    @ApiModelProperty("企业ID-精确搜索")
    private Long eid;

    @ApiModelProperty("企业名称-模糊搜索")
    private String ename;

    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    @ApiModelProperty("所属城市编码")
    private String cityCode;

    @ApiModelProperty("所属区域编码")
    private String regionCode;
}
