package com.yiling.open.erp.entity;

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
 * 流向非以岭商品配置
 *
 * @author: houjie.sun
 * @date: 2022/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_flow_goods_config")
public class ErpFlowGoodsConfigDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商业ID
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品内码（供应商的ERP的商品主键）
     */
    private String goodsInSn;

    /**
     * 商品规格
     */
    private String specifications;

    /**
     * 批准文号（注册证号）
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

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
