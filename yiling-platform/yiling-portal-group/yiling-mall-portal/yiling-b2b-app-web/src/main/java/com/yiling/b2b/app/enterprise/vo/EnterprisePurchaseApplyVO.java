package com.yiling.b2b.app.enterprise.vo;

import java.util.List;

import com.yiling.b2b.app.shop.vo.EnterpriseCertificateVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业采购-供应商信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-01-17
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EnterprisePurchaseApplyVO {

    /**
     * 供应商企业ID
     */
    @ApiModelProperty("供应商企业ID")
    private Long eid;

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String name;

    /**
     * 店铺描述
     */
    @ApiModelProperty("店铺描述")
    private String shopDesc;

    /**
     * 店铺logo
     */
    @ApiModelProperty("店铺logo")
    private String shopLogo;

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
     * 企业类型:1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    @ApiModelProperty("企业类型:1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所")
    private Integer type;

    /**
     * 审核状态：1-待审核 2-已建采 3-已驳回 4-未建采(此状态4为虚拟状态，仅展示给前端使用)
     */
    @ApiModelProperty("审核状态：1-待审核 2-已建采 3-已驳回 4-未建采（此状态4可申请建立采购关系）")
    private Integer authStatus;

    /**
     * 驳回原因
     */
    @ApiModelProperty("驳回原因")
    private String authRejectReason;

    /**
     * 销售省份名称集合
     */
    @ApiModelProperty("销售省份名称集合")
    private List<String> salesProvinceNameList;

    /**
     * 企业资质列表
     */
    @ApiModelProperty("企业资质列表")
    private List<EnterpriseCertificateVO> certificateVOList;

}
