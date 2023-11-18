package com.yiling.admin.b2b.integral.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询订单送积分-指定客户分页列表 Form
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralGiveEnterprisePageForm extends QueryPageListForm {

    /**
     * 发放规则id
     */
    @NotNull
    @ApiModelProperty(value = "发放规则id", required = true)
    private Long giveRuleId;

    /**
     * 企业ID-精确搜索
     */
    @ApiModelProperty("企业ID-精确搜索")
    private Long eid;

    /**
     * 企业ID-精确搜索
     */
    @ApiModelProperty("企业ID-精确搜索")
    private List<Long> eidList;

    /**
     * 企业名称-模糊搜索
     */
    @ApiModelProperty("企业名称-模糊搜索")
    private String ename;
}
