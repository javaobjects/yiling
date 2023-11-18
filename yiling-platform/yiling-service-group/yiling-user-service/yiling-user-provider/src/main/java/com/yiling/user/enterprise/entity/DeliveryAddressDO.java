package com.yiling.user.enterprise.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配送地址表
 * </p>
 *
 * @author gxl
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("delivery_address")
@ToString
public class DeliveryAddressDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 收货人手机号
     */
    private String mobile;

    /**
     * 省份编码
     */
    @Deprecated
    private String provinceCode;

    /**
     * 省份名称
     */
    @Deprecated
    private String provinceName;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    @Deprecated
    private String cityName;

    /**
     * 区域编码
     */
    @Deprecated
    private String regionCode;

    /**
     * 区域名称
     */
    @Deprecated
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 是否默认：0-否 1-是
     */
    private Integer defaultFlag;

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
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 更新日期
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
