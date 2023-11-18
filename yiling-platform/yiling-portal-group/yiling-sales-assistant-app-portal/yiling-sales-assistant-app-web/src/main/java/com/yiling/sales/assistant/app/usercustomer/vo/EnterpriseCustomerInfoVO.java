package com.yiling.sales.assistant.app.usercustomer.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 客户信息
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.vo
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseCustomerInfoVO extends BaseVO {

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String   name;


    /**
     * 客户企业ID
     */
    @ApiModelProperty("客户企业ID")
    private Long customerEid;

    /**
     * 联系人
     */
    @ApiModelProperty("联系人")
    private String   contactor;

    /**
     * 区号-号码
     */
    @ApiModelProperty("区号-号码")
    private String   contactorPhone;

    /**
     * 所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String provinceName;

    /**
     * 市
     */
    @ApiModelProperty("市")
    private String   cityName;

    /**
     * 区域
     */
    @ApiModelProperty("区域")
    private String   regionName;

    /**
     * 企业地址
     */
    @ApiModelProperty("企业地址")
    private String   address;

    /**
     * 是否为禁用客户：1-启用 2-禁用
     */
    @ApiModelProperty("是否为禁用客户：1-启用 2-禁用（禁用后：禁止参与本次推广活动）")
    private Integer customerStatus;

    /**
     * 企业信用代码
     */
    @ApiModelProperty(value = "企业信用代码", hidden = true)
    private String licenseNumber;


}
