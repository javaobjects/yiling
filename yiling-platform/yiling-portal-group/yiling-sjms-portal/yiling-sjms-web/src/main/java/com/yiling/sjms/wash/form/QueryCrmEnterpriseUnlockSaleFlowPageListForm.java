package com.yiling.sjms.wash.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseUnlockSaleFlowPageListForm extends QueryPageListForm {

    @ApiModelProperty(value = "机构基础信息id")
    private Long crmEnterpriseId;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "供应链角色：1-商业公司 2-医疗机构 3-零售机构")
    private Integer supplyChainRole;


    @ApiModelProperty(value = "所属省份编码")
    private String provinceCode;

    @ApiModelProperty(value = "所属城市编码")
    private String cityCode;

    @ApiModelProperty(value = "所属区域编码")
    private String regionCode;

    /**------   业务字段信息 -------*/
    @ApiModelProperty(value = "非锁销量分配商业公司弹框规则ID")
    private Long businessRuleId;
    @ApiModelProperty(value = "非锁销量分配客户公司弹框规则ID")
    private Long customerRuleId;

}
