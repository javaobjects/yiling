package com.yiling.data.center.admin.enterprisecustomer.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业客户列表项 VO
 *
 * @author: lun.yu
 * @date: 2021/10/29
 */
@Data
public class EnterpriseCustomerListItemVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 客户企业ID
     */
    @ApiModelProperty("客户企业ID")
    private Long customerEid;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户企业名称")
    private String customerName;

    /**
     * 企业类型
     */
    @ApiModelProperty("企业类型")
    private Integer type;

    /**
     * 客户类型
     */
    @ApiModelProperty("客户类型")
    private String customerType;

    /**
     * ERP客户名称
     */
    @ApiModelProperty("ERP客户名称")
    private String customerErpName;

    /**
     * ERP内码
     */
    @ApiModelProperty("ERP内码")
    private String customerErpCode;

    /**
     * ERP编码
     */
    @ApiModelProperty("ERP编码")
    private String customerCode;

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
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String provinceName;

    /**
     * 所属城市编码
     */
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属城市名称
     */
    @ApiModelProperty("所属城市名称")
    private String cityName;

    /**
     * 所属区域编码
     */
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 所属区域名称
     */
    @ApiModelProperty("所属区域名称")
    private String regionName;

    /**
     * 企业地址
     */
    @ApiModelProperty("企业地址")
    private String address;

    /**
     * 数据来源：1-后台导入 2-ERP对接 3-SAAS导入 4-协议生成
     */
    @ApiModelProperty("数据来源：1-后台导入 2-ERP对接 3-SAAS导入 4-协议生成")
    private Integer source;

    /**
     * 客户分组ID
     */
    @ApiModelProperty("客户分组ID")
    private Long customerGroupId;

    /**
     * 客户分组名称
     */
    @ApiModelProperty("客户分组名称")
    private String customerGroupName;

}
