package com.yiling.admin.b2b.strategy.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 简单企业信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseVO extends BaseVO {

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
     * 渠道类型（数据字典：channel_type）
     */
    @ApiModelProperty("渠道类型（数据字典：channel_type）")
    private Long channelId;

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
     * 审核人姓名
     */
    @ApiModelProperty("审核人姓名")
    private String authUserName;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private Date authTime;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

    /**
     * ERP对接级别：0-未对接 1-基础对接 2-订单对接 3-发货单对接
     */
    @ApiModelProperty("ERP对接级别：0-未对接 1-基础对接 2-订单对接 3-发货单对接")
    private Integer erpSyncLevel;

    /**
     * HMC类型（数据字典：hmc_type）
     */
    @ApiModelProperty("HMC类型（数据字典：hmc_type）")
    private Integer hmcType;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改人姓名
     */
    @ApiModelProperty("修改人姓名")
    private String updateUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
}
