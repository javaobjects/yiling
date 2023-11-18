package com.yiling.b2b.admin.enterprisecustomer.vo;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业客户详情 VO
 *
 * @author: lun.yu
 * @date: 2021/11/1
 */
@Data
public class CustomerDetailVO {

    @ApiModelProperty("客户信息")
    private CustomerVO customerInfo;

    @ApiModelProperty("客户支付方式设置")
    private List<PaymentMethodVO> paymentMethodList;

    @ApiModelProperty("ERP信息")
    private ErpCustomerVO erpCustomerInfo;

    @ApiModelProperty("资质信息")
    private List<EnterpriseCertificateVO> certificateList;

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

        @ApiModelProperty("是否禁用")
        private Boolean disabled;
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
    public static class EnterpriseCertificateVO {

        @ApiModelProperty("ID")
        private Long id;
        /**
         * 资质类型（参见EnterpriseCertificateTypeEnum）
         */
        @ApiModelProperty("资质类型（见字典）")
        private Integer type;

        /**
         * 资质文件KEY
         */
        @ApiModelProperty("资质文件KEY")
        private String fileKey;

        /**
         * 资质Url
         */
        @ApiModelProperty("资质Url")
        private String fileUrl;

        /**
         * 资质是否必填：0-否 1-是
         */
        @ApiModelProperty("资质是否必填：0-否 1-是")
        private Integer mustExist;

        /**
         * 资质有效期-起
         */
        @ApiModelProperty("资质有效期-起")
        private Date periodBegin;

        /**
         * 资质有效期-止
         */
        @ApiModelProperty("资质有效期-止")
        private Date periodEnd;

        /**
         * 是否长期有效：0-否 1-是
         */
        @ApiModelProperty("是否长期有效：0-否 1-是")
        private Integer longEffective;

        /**
         * 证照日期是否必填：0-否 1-是
         */
        @ApiModelProperty("证照日期是否必填：0-否 1-是")
        private Integer mustPicDate;

    }

}
