package com.yiling.payment.basic.entity;

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
 * 银行表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bank")
public class BankDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 总行名称
     */
    private String headName;

    /**
     * 总行简称
     */
    private String headSimpleName;

    /**
     * 总行编码
     */
    private String headCode;

    /**
     * 总行行号
     */
    private String headNum;

    /**
     * 支行行号
     */
    private String branchName;

    /**
     * 支行名称
     */
    private String branchNum;

    /**
     * 银行类型 1-总行 2-支行
     */
    private Integer type;

    /**
     * 省code
     */
    private String provinceCode;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市code
     */
    private String cityCode;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
