package com.yiling.sales.assistant.app.order.form;

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

   /* @Min(value = 1, groups = { YilingOrderSubmitValidateGroup.class })
    @NotNull(groups = { YilingOrderSubmitValidateGroup.class })
    @ApiModelProperty(value = "部门ID")
    private Long deptId;*/

    @NotNull(groups = { YilingOrderSubmitValidateGroup.class,CommonOrderSubmitValidateGroup.class})
    @Min(value = 1, groups = { YilingOrderSubmitValidateGroup.class,CommonOrderSubmitValidateGroup.class })
    @ApiModelProperty(value = "选择客户ID")
    private Long customerEid;

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

        @Length(max = 20, groups = { YilingOrderSubmitValidateGroup.class})
        @NotNull(groups = { YilingOrderSubmitValidateGroup.class })
        @ApiModelProperty("合同编号")
        private String contractNumber;

        @NotEmpty(groups = { YilingOrderSubmitValidateGroup.class })
        @ApiModelProperty("销售合同文件KEY列表")
        private List<String> contractFileKeyList;

        @Length(max = 200, groups = { YilingOrderSubmitValidateGroup.class, CommonOrderSubmitValidateGroup.class })
        @ApiModelProperty(value = "买家留言")
        private String buyerMessage;
    }

    public interface YilingOrderSubmitValidateGroup {
    }
    public interface CommonOrderSubmitValidateGroup {
    }
}
