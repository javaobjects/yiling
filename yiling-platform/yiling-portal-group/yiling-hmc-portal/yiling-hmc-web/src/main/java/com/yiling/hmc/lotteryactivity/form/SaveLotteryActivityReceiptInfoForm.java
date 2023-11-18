package com.yiling.hmc.lotteryactivity.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加抽奖活动收货信息 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveLotteryActivityReceiptInfoForm extends BaseForm {

    /**
     * 参与抽奖明细ID
     */
    @NotNull
    @ApiModelProperty(value = "参与抽奖明细ID（我的奖品列表的ID）", required = true)
    private Long joinDetailId;

    /**
     * 收货人
     */
    @NotEmpty
    @ApiModelProperty(value = "收货人", required = true)
    private String contactor;

    /**
     * 联系电话
     */
    @NotEmpty
    @ApiModelProperty(value = "联系电话", required = true)
    private String contactorPhone;

    /**
     * 所属省份编码
     */
    @NotEmpty
    @ApiModelProperty(value = "所属省份编码", required = true)
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @NotEmpty
    @ApiModelProperty(value = "所属省份名称", required = true)
    private String provinceName;

    /**
     * 所属城市编码
     */
    @NotEmpty
    @ApiModelProperty(value = "所属城市编码", required = true)
    private String cityCode;

    /**
     * 所属城市名称
     */
    @NotEmpty
    @ApiModelProperty(value = "所属城市名称", required = true)
    private String cityName;

    /**
     * 所属区域编码
     */
    @NotEmpty
    @ApiModelProperty(value = "所属区域编码", required = true)
    private String regionCode;

    /**
     * 所属区域名称
     */
    @NotEmpty
    @ApiModelProperty(value = "所属区域名称", required = true)
    private String regionName;

    /**
     * 详细地址
     */
    @NotEmpty
    @ApiModelProperty(value = "详细地址", required = true)
    private String address;

}
