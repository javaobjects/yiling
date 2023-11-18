package com.yiling.data.center.admin.enterprise.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseSimpleVO extends BaseVO {

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String name;

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
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    @ApiModelProperty("类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所")
    private Integer type;

    /**
     * 认证状态：1-未认证 2-认证通过 3-认证不通过
     */
    @ApiModelProperty("认证状态：1-未认证 2-认证通过 3-认证不通过")
    private Integer authStatus;

    /**
     * 企业副本的认证状态：1-未认证 2-认证通过 3-认证不通过
     */
    @ApiModelProperty("企业副本的认证状态：1-未认证 2-认证通过 3-认证不通过(为1展示：提交成功，请等待审核；为3展示：审核失败，请重新提交)")
    private Integer transcriptAuthStatus;

    /**
     * 审核拒绝原因
     */
    @ApiModelProperty("审核拒绝原因")
    private String authRejectReason;

    /**
     * 是否可以编辑
     */
    @ApiModelProperty(value = "是否可以编辑：0-否 1-是（审核中不可以编辑）")
    private Integer editStatus;

    /**
     * 企业资质列表
     */
    @ApiModelProperty("企业资质列表")
    private List<EnterpriseCertificateVO> certificateVoList;


}
