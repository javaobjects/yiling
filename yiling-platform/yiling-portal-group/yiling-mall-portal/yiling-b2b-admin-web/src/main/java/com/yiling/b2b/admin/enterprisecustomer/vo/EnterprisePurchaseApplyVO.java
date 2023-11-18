package com.yiling.b2b.admin.enterprisecustomer.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业采购申请-采购商信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-17
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EnterprisePurchaseApplyVO extends BaseVO {

    /**
     * 供应商企业ID
     */
    @ApiModelProperty("采购商企业ID")
    private Long customerEid;

    /**
     * 供应商名称
     */
    @ApiModelProperty("采购商名称")
    private String name;

    /**
     * 店铺描述
     */
    @ApiModelProperty("店铺描述")
    private String shopDesc;

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
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

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
    @ApiModelProperty("")
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
     * 企业类型:1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    @ApiModelProperty("企业类型:1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所")
    private Integer type;

    /**
     * 审核状态：1-待审核 2-已建采 3-已驳回
     */
    @ApiModelProperty("审核状态：1-待审核 2-已建采 3-已驳回")
    private Integer authStatus;

    /**
     * 驳回原因
     */
    @ApiModelProperty("驳回原因")
    private String authRejectReason;

    /**
     * 提交时间
     */
    @ApiModelProperty("提交时间")
    private Date updateTime;

    /**
     * 企业资质列表
     */
    @ApiModelProperty("企业资质列表")
    private List<EnterpriseCertificateVO> certificateVOList;

}
