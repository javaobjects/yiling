package com.yiling.f2b.admin.enterprise.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业客户列表项 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
@Data
public class CustomerListItemVO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 客户ID
     */
    @ApiModelProperty("客户ID")
    private Long customerEid;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String customerName;

    /**
     * 客户类型
     */
    @ApiModelProperty("客户类型")
    private String customerType;

    /**
     * 客户编码
     */
    @ApiModelProperty("客户编码")
    private String customerCode;

    /**
     * 客户ERP编码
     */
    @ApiModelProperty("客户ERP编码")
    private String customerErpCode;

    /**
     * 客户分组ID
     */
    @ApiModelProperty("客户分组ID")
    private Long customerGroupId;

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
     * 通讯地址
     */
    @ApiModelProperty("通讯地址")
    private String address;

    /**
     * 数据来源：1-后台导入 2-ERP对接 3-SAAS导入 4-协议生成
     */
    @ApiModelProperty("数据来源：1-后台导入 2-ERP对接 3-SAAS导入 4-协议生成")
    private Integer source;

    /**
     * 采购次数
     */
    @ApiModelProperty("采购次数")
    private Long purchaseNumber;

    /**
     * 支付方式
     */
    @ApiModelProperty("支付方式")
    private List<String> paymentMethods;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 是否已添加：true-是，false-否
     */
    @ApiModelProperty("是否已添加：true-是，false-否")
    private Boolean selectedFlag;

    public CustomerListItemVO() {
        this.selectedFlag = false;
    }

}
