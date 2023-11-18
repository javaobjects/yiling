package com.yiling.user.enterprise.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业信息表
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("enterprise")
public class EnterpriseDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业简称
     */
    private String shortName;

    /**
     * 企业Logo
     */
    private String logo;

    /**
     * 企业编码
     */
    private String code;

    /**
     * ERP编码
     */
    private String erpCode;

    /**
     * 法人姓名
     */
    private String legal;

    /**
     * 法人身份证号
     */
    private String legalIdNumber;

    /**
     * 法人授权委托书
     */
    private String legalAttorney;

    /**
     * 联系人
     */
    private String contactor;

    /**
     * 联系人电话
     */
    private String contactorPhone;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 所属企业ID
     */
    private Long parentId;

    /**
     * 渠道ID
     */
    private Long channelId;

    /**
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    private Integer type;

    /**
     * 来源：1-后台导入 2-网页注册 3-APP注册
     */
    private Integer source;

    /**
     * 认证状态：1-未认证 2-认证通过 3-认证不通过 4-认证中
     */
    private Integer authStatus;

    /**
     * 审核人
     */
    private Long authUser;

    /**
     * 审核时间
     */
    private Date authTime;

    /**
     * 审核驳回原因
     */
    private String authRejectReason;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 是否对接ERP：0-否 1-是 （已废弃）
     */
    private Integer erpSyncFlag;

    /**
     * ERP对接级别：0-未对接 1-基础对接 2-订单对接 3-发货单对接
     */
    private Integer erpSyncLevel;

    /**
     * 系统处理标记：0-未处理 1-正确 2-异常
     */
    private Integer handleType;

    /**
     * 相识度
     */
    private Integer likeValue;

    /**
     * HMC业务类型：1-药+险销售 2-药+险销售与药品兑付
     */
    private Integer hmcType;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
