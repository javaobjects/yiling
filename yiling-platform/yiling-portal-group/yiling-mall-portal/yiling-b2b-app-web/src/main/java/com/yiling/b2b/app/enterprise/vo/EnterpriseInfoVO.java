package com.yiling.b2b.app.enterprise.vo;

import java.util.List;

import com.yiling.b2b.app.shop.vo.EnterpriseCertificateVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业基本信息 VO
 *
 * @author: lun.yu
 * @date: 2021/10/21
 */
@Data
public class EnterpriseInfoVO implements java.io.Serializable {

    /**
     * 企业类型
     */
    @ApiModelProperty(value = "企业类型")
    private Integer type;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String name;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;

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
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty(value = "执业许可证号/社会信用统一代码")
    private String licenseNumber;

    /**
     * 认证状态：1-未认证 2-认证通过 3-认证不通过
     */
    @ApiModelProperty(value = "认证状态：1-未认证 2-认证通过 3-认证不通过")
    private Integer authStatus;

    /**
     * 企业副本的认证状态：1-未认证 2-认证通过 3-认证不通过
     */
    @ApiModelProperty("企业副本的认证状态：1-未认证 2-认证通过 3-认证不通过(为1展示：提交成功，请等待审核；为3展示：审核失败，请重新提交)")
    private Integer transcriptAuthStatus;

    /**
     * 审核驳回原因
     */
    @ApiModelProperty(value = "审核驳回原因")
    private String authRejectReason;

    /**
     * 是否可以编辑
     */
    @ApiModelProperty(value = "是否可以编辑：0-否 1-是（审核中不可以编辑）")
    private Integer editStatus;

    /**
     * 企业资质集合
     */
    @ApiModelProperty(value = "企业资质集合")
    private List<EnterpriseCertificateVO> certificateVOList;

}
