package com.yiling.sales.assistant.app.usercustomer.vo;

import java.io.Serializable;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户客户详情VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/18
 */
@Data
@ApiModel
public class UserCustomerDetailVO extends BaseVO implements Serializable {

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "企业类型")
    private Integer type;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String   name;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String   contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String   contactorPhone;

    /**
     * 社会统一信用代码
     */
    @ApiModelProperty(value = "社会统一信用代码")
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    @ApiModelProperty(value = "所属省份编码")
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @ApiModelProperty(value = "所属省份名称")
    private String provinceName;

    /**
     * 所属城市编码
     */
    @ApiModelProperty(value = "所属城市编码")
    private String cityCode;

    /**
     * 所属城市名称
     */
    @ApiModelProperty(value = "所属城市名称")
    private String cityName;

    /**
     * 所属区域编码
     */
    @ApiModelProperty(value = "所属区域编码")
    private String regionCode;

    /**
     * 所属区域名称
     */
    @ApiModelProperty(value = "所属区域名称")
    private String regionName;
    /**
     * 企业地址
     */
    @ApiModelProperty(value = "企业地址")
    private String   address;

    /**
     * 状态：1-待审核 2-审核通过 3-审核驳回
     */
    @ApiModelProperty(value = "状态：1-待审核 2-审核通过 3-审核驳回")
    private Integer status;

    /**
     * 驳回原因
     */
    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;

    /**
     * 企业资质列表
     */
    @ApiModelProperty(value = "企业资质列表")
    private List<EnterpriseCertificateVO> certificateList;

    /**
     * 企业额度信息
     */
    @ApiModelProperty(value = "企业额度信息")
    private List<PaymentDaysAccountListItemVO> paymentDaysAccountListItemVO;

}
