package com.yiling.f2b.admin.enterprise.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 渠道商详情页
 *
 * @author: yuecheng.chen
 * @date: 2021/6/7 0007
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ChannelCustomerDetailVO extends BaseVO {
    /**
     * 认证状态：1-未认证 2-认证通过 3-认证不通过
     */
    @ApiModelProperty("认证状态：1-未认证 2-认证通过 3-认证不通过")
    private Integer authStatus;

    /**
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    @ApiModelProperty("类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所")
    private Integer type;

    /**
     * 渠道Id
     */
    @ApiModelProperty("渠道ID")
    private Long channelId;

    /**
     * 渠道名称
     */
    @ApiModelProperty("渠道名称")
    private String channelName;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;

    /**
     * 商务联系人列表
     */
    @ApiModelProperty("商务联系人列表")
    private List<CustomerContactVO> customerContacts;

    /**
     * 客户支付方式列表
     */
    @ApiModelProperty("客户支付方式列表")
    private List<PaymentMethodVO> customerPaymentMethods;

    /**
     * 客户分组信息
     */
    @ApiModelProperty("客户分组信息")
    private CustomerGroupVO customerGroup;

    @ApiModelProperty("EAS信息")
    private List<EasInfoVO> easInfoList;

    /**
     * 采购关系列表
     */
    @ApiModelProperty("采购关系列表")
    private List<PurchaseRelationVO> purchaseRelations;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("企业ID")
    private Long eid;

    @Data
    public static class PaymentMethodVO {

        @ApiModelProperty("支付方式ID")
        private Long id;

        @ApiModelProperty("支付方式名称")
        private String name;

        @ApiModelProperty("支付方式备注")
        private String remark;

        @ApiModelProperty("是否选中")
        private Boolean checked;
    }

    @Data
    public static class EasInfoVO {

        @ApiModelProperty("ID")
        private Long id;

        @ApiModelProperty("EAS客户名称")
        private String easName;

        @ApiModelProperty("EAS编码")
        private String easCode;
    }
}
