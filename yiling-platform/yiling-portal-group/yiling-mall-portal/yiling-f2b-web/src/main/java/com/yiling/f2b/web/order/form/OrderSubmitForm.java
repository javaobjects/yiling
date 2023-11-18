package com.yiling.f2b.web.order.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提交订单 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/24
 */
@Data
public class OrderSubmitForm {

    @NotNull(groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
    @Min(value = 1, groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
    @ApiModelProperty(value = "收货地址ID", required = true)
    private Long addressId;

    @NotEmpty(groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
    @ApiModelProperty(value = "配送商订单列表", required = true)
    private List<DistributorOrderForm> distributorOrderList;

    @NotNull(groups = { YilingOrderSubmitValidateGroup.class,CommonOrderSubmitValidateGroup.class })
    @Min(value = 1, groups = { YilingOrderSubmitValidateGroup.class })
    @ApiModelProperty(value = "商务联系人ID")
    private Long contacterId;

    @ApiModelProperty(value = "部门Id")
    private Long departmentId;

    @NotEmpty(groups = { YilingOrderSubmitValidateGroup.class })
    @ApiModelProperty(value = "企业账号")
    private String easAccount;

    /**
     * 配送商订单 Form
     */
    @Data
    public static class DistributorOrderForm {

        @NotNull(groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
        @Min(value = 1, groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
        @ApiModelProperty(value = "配送商企业ID", required = true)
        private Long distributorEid;

        @NotEmpty(groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
        @ApiModelProperty(value = "购物车ID集合", required = true)
        private List<Long> cartIds;

        @NotNull(groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
        @Min(value = 1, groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
        @ApiModelProperty(value = "配送商支付方式：1-线下支付 2-在线支付", required = true)
        private Integer paymentType;

        @NotNull(groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
        @Min(value = 1, groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
        @ApiModelProperty(value = "卖家支付方式", required = true)
        private Integer paymentMethod;

        @NotEmpty(groups = { YilingOrderSubmitValidateGroup.class })
        @ApiModelProperty("销售合同文件KEY列表")
        private List<String> contractFileKeyList;

        @Length(max = 20, groups = { YilingOrderSubmitValidateGroup.class})
        @ApiModelProperty("合同编号")
        private String contractNumber;

        @Length(max = 200, groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
        @ApiModelProperty(value = "买家留言")
        private String buyerMessage;
    }

    public interface YilingOrderSubmitValidateGroup {
    }
    public interface CommonOrderSubmitValidateGroup {
    }
}
