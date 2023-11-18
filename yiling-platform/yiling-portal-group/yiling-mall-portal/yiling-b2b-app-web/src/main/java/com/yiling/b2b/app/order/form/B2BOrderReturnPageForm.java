package com.yiling.b2b.app.order.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 手机app查询售后信息的请求参数
 *
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2BOrderReturnPageForm extends QueryPageListForm {

//    @ApiModelProperty(value = "当前操作人", required = true)
//    private Long currentUserId;

    @ApiModelProperty(value = "退货单号和供应商名称  退货号-精确查询，供应商名称-模糊查询")
    private String condition;

    @ApiModelProperty(value = "退货单状态：0-全部 1-待审核 2-已通过 3-已驳回")
    private Integer returnStatus;
}
