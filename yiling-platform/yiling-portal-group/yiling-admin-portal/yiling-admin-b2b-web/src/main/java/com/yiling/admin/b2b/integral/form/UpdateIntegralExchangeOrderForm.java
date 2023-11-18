package com.yiling.admin.b2b.integral.form;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换订单 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateIntegralExchangeOrderForm extends BaseForm {

    /**
     * 兑付类型：1-兑付全部 2-兑付当前页 3-单个兑付
     */
    @NotNull
    @Range(min = 1, max = 3)
    @ApiModelProperty(value = "兑付类型：1-兑付全部 2-兑付当前页 3-单个兑付", required = true)
    private Integer exchangeType;

    /**
     * 兑付订单ID
     */
    @ApiModelProperty(value = "兑付订单ID（单个兑付）")
    private Long id;

    /**
     * 兑付订单集合
     */
    @ApiModelProperty(value = "兑付订单集合（兑付当前页）")
    private List<Long> idList;

    /**
     * 收货信息
     */
    @ApiModelProperty("收货信息（真实物品兑付时使用）")
    private ReceiptInfoForm orderReceiptInfo;

    @Data
    private static class ReceiptInfoForm {

        /**
         * 收货人
         */
        @ApiModelProperty("收货人")
        private String contactor;

        /**
         * 联系电话
         */
        @ApiModelProperty("联系电话")
        private String contactorPhone;

        /**
         * 所属省份名称
         */
        @ApiModelProperty("所属省份名称")
        private String provinceName;

        /**
         * 所属省份编码
         */
        @ApiModelProperty("所属省份编码")
        private String provinceCode;

        /**
         * 所属城市名称
         */
        @ApiModelProperty("所属城市名称")
        private String cityName;

        /**
         * 所属城市编码
         */
        @ApiModelProperty("所属城市编码")
        private String cityCode;

        /**
         * 所属区域名称
         */
        @ApiModelProperty("所属区域名称")
        private String regionName;

        /**
         * 所属区域编码
         */
        @ApiModelProperty("所属区域编码")
        private String regionCode;

        /**
         * 详细地址
         */
        @ApiModelProperty("详细地址")
        private String address;

        /**
         * 快递公司（见字典）
         */
        @ApiModelProperty("快递公司（见字典）")
        private String expressCompany;

        /**
         * 快递单号
         */
        @ApiModelProperty("快递单号")
        private String expressOrderNo;

    }

}
