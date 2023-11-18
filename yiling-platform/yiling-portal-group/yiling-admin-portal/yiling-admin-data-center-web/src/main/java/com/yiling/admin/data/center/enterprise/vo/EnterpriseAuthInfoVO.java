package com.yiling.admin.data.center.enterprise.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业副本信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/3
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseAuthInfoVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

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
     * 审核人
     */
    @ApiModelProperty("审核人")
    private Long authUser;

    /**
     * 审核人名称
     */
    @ApiModelProperty("审核人名称")
    private String authUserName;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private Date authTime;

    /**
     * 审核驳回原因
     */
    @ApiModelProperty("审核驳回原因")
    private String authRejectReason;

    /**
     * 数据来源：1-B2B 2-销售助手 3-企业信息更新
     */
    @ApiModelProperty("数据来源：1-B2B 2-销售助手 3-企业信息更新")
    private Integer source;

    /**
     * 审核类型：1-首次认证 2-资质更新 3-驳回后再次认证
     */
    @ApiModelProperty("审核类型：1-首次认证 2-资质更新 3-驳回后再次认证")
    private Integer authType;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 企业资质列表
     */
    @ApiModelProperty("企业资质列表")
    private List<EnterpriseCertificateVO> certificateVOList;


}
