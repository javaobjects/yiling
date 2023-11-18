package com.yiling.admin.b2b.lotteryactivity.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新抽奖活动收货信息 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateLotteryActivityReceiptInfoForm extends BaseForm {

    /**
     * 参与抽奖明细ID
     */
    @ApiModelProperty("参与抽奖明细ID")
    private Long joinDetailId;

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
