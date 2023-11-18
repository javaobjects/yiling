package com.yiling.hmc.admin.order.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnPageForm extends QueryPageListForm {

    @ApiModelProperty("退货单编号")
    private String returnNo;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("第三方退单编号")
    private String thirdReturnNo;

    @ApiModelProperty("药品服务终端id")
    private Long eid;

    @ApiModelProperty("药品服务终端名称")
    private String ename;
}
