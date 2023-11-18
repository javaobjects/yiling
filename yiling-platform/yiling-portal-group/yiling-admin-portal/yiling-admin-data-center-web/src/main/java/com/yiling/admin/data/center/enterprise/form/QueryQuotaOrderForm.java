package com.yiling.admin.data.center.enterprise.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询账期订单 Form
 *
 * @author: tingwei.chen
 * @date: 2021/7/5
 */
@Data
@Accessors(chain = true)
public class QueryQuotaOrderForm extends QueryPageListForm {
    /**
     * 账户Id
     */
    @NotNull
    @ApiModelProperty(value = "账户Id", required = true)
    private Long accountId;

    /**
     * 还款状态：1-未还款 2-部分还款 3-全部还款
     */
    @Range(min = 0,max = 3)
    @ApiModelProperty(value = "还款状态：1-未还款 2-部分还款 3-全部还款")
    private Integer status;

    /**
     * 菜单类型：1.已使用订单，2.已还款订单
     */
    @NotNull
    @ApiModelProperty(value = "菜单类型：1.已使用订单，2.已还款订单", required = true)
    private Integer type;

    /**
     * 审核
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 审核
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
