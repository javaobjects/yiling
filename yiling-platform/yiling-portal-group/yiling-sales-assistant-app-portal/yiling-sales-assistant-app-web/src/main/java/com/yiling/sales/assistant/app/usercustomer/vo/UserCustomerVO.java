package com.yiling.sales.assistant.app.usercustomer.vo;

import java.io.Serializable;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户客户信息VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/18
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserCustomerVO extends BaseVO implements Serializable {

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String   name;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String   contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String   contactorPhone;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String   provinceName;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String   cityName;

    /**
     * 区域
     */
    @ApiModelProperty(value = "区域")
    private String   regionName;

    /**
     * 企业地址
     */
    @ApiModelProperty(value = "企业地址")
    private String   address;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回
     */
    @ApiModelProperty(value = "状态：1-待审核 2-审核通过 3-审核驳回")
    private Integer status;

    /**
     * 驳回原因
     */
    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;

    /**
     * 是否为禁用客户：1-启用 2-禁用
     */
    @ApiModelProperty("是否为禁用客户：1-启用 2-禁用（禁用后：禁止参与本次推广活动）")
    private Integer customerStatus;

    /**
     * 企业信用代码
     */
    @ApiModelProperty(value = "企业信用代码")
    private String licenseNumber;

}
