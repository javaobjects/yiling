package com.yiling.admin.b2b.integral.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 删除订单送积分-指定客户 Form
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteIntegralGiveEnterpriseForm extends BaseForm {

    /**
     * 发放规则ID
     */
    @NotNull
    @ApiModelProperty(value = "发放规则ID", required = true)
    private Long giveRuleId;

    /**
     * 企业id-单独删除时使用
     */
    @ApiModelProperty("企业id-单独删除时使用")
    private Long eid;

    /**
     * 企业id集合-批量删除时使用
     */
    @ApiModelProperty("企业id集合-批量删除时使用")
    private List<Long> eidList;

    /**
     * 企业ID-精确搜索-删除搜索结果时使用
     */
    @ApiModelProperty("企业ID-精确搜索-删除搜索结果时使用")
    private Long eidPage;

    /**
     * 企业名称-模糊搜索-删除搜索结果时使用
     */
    @ApiModelProperty("企业名称-模糊搜索-删除搜索结果时使用")
    private String enamePage;
}
