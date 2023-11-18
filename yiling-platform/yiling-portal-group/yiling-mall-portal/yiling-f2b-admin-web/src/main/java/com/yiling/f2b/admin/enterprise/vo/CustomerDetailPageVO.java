package com.yiling.f2b.admin.enterprise.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业客户详情页 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/24
 */
@Data
public class CustomerDetailPageVO {

    @ApiModelProperty("客户信息")
    private CustomerVO customerInfo;

    @ApiModelProperty("客户支付方式设置")
    private List<PaymentMethodVO> paymentMethodList;

    @ApiModelProperty("账期额度信息")
    private PaymentDaysVO paymentDaysInfo;

    @ApiModelProperty("ERP信息")
    private ErpCustomerVO erpCustomerInfo;

    @ApiModelProperty("是否显示EAS信息")
    private boolean showEasInfoFlag;

    @ApiModelProperty("EAS信息")
    private List<EasInfoVO> easInfoList;

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
    public static class PaymentDaysVO {

        @ApiModelProperty("账期额度")
        private BigDecimal totalAmount;

        @ApiModelProperty("账期有效时间-起")
        private Date startTime;

        @ApiModelProperty("账期有效时间-止")
        private Date endTime;

        @ApiModelProperty("还款周期（天）")
        private Integer period;
    }

    @Data
    public static class ErpCustomerVO {

        @ApiModelProperty("ERP客户名称")
        private String customerName;

        @ApiModelProperty("ERP客户编码")
        private String customerCode;

        @ApiModelProperty("ERP客户内码")
        private String customerErpCode;
    }

    @Data
    public static class EasInfoVO {

        @ApiModelProperty("EAS客户名称")
        private String easName;

        @ApiModelProperty("EAS编码")
        private String easCode;
    }
}
