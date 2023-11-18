package com.yiling.sales.assistant.app.paymentdays.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 企业客户信息VO
 * </p>
 *
 * @author lun.yu
 * @date 2021-09-28
 */
@Data
@ApiModel
public class EnterpriseCustomerVO extends BaseVO {

    private static final long serialVersionUID = -8644310526425389368L;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 联系人
     */
    @ApiModelProperty("联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty("联系人电话")
    private String contactorPhone;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;

}
