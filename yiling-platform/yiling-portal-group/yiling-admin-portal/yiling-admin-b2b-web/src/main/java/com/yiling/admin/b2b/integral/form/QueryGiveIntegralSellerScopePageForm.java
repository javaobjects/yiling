package com.yiling.admin.b2b.integral.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单送积分-指定商家-已添加商家分页列表查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGiveIntegralSellerScopePageForm extends QueryPageListForm {

    @NotNull
    @ApiModelProperty(value = "发放规则ID", required = true)
    private Long giveRuleId;

    @ApiModelProperty("企业ID")
    private Long eid;

    @ApiModelProperty("企业名称")
    private String ename;

    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    @ApiModelProperty("所属城市编码")
    private String cityCode;

    @ApiModelProperty("所属区域编码")
    private String regionCode;

    @ApiModelProperty("标签ID列表")
    private List<Long> tagIds;
}
