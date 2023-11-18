package com.yiling.goods.medicine.entity;

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
 * 商品待审核表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_audit")
public class GoodsAuditDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long gid;

    /**
     * 供应商ID
     */
    private Long eid;

    /**
     * 供应商名称
     */
    private String ename;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 通用名称
     */
    private String commonName;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 生产地址
     */
    private String manufacturerAddress;

    /**
     * 销售规格
     */
    private String specifications;

    /**
     * 单位
     */
    private String unit;

    /**
     * 审核状态：0待审核1审核通过2审核不通3忽略
     */
    private Integer auditStatus;

    /**
     * 驳回信息
     */
    private String rejectMessage;

    /**
     * 数据来源：1pop平台 2商城平台 3ERP对接
     */
    private Integer source;

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
