package com.yiling.sjms.wash.form;


import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/5/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateUnlockFlowWashSaleDistributionForm extends BaseForm {

    /**
     * 销量计入规则：1-指定部门2-指定部门+省区3-商业公司三者关系4-商业公司负责人
     */
    @ApiModelProperty("销量计入规则：1-指定部门2-指定部门+省区3-商业公司三者关系4-商业公司负责人")
    private Integer saleRange;

    @ApiModelProperty("销量计入部门对象")
    private UnlockSaleDepartmentForm unlockSaleDepartment;

    /**
     * 非锁分配备注
     */
    @ApiModelProperty("非锁分配备注")
    private String notes;

    /**
     * 判断：1-不计入2-公司发货不计入3-漏做客户关系对照商业客户4-不参与计算品种5-当月非锁销量6-商业客户数据计入营销中心7-政府机构
     */
    @ApiModelProperty("判断：1-不计入2-公司发货不计入3-漏做客户关系对照商业客户4-不参与计算品种5-当月非锁销量6-商业客户数据计入营销中心7-政府机构")
    private Integer judgment;

    @ApiModelProperty("非锁流向数据主键集合ID")
    private List<Long> ids;
}
