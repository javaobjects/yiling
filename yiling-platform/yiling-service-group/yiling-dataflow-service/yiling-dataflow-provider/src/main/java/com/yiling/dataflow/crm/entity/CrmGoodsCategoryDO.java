package com.yiling.dataflow.crm.entity;

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
 * @author shichen
 * @类名 CrmGoodsCategoryDO
 * @描述 crm商品品类
 * @创建时间 2023/4/6
 * @修改人 shichen
 * @修改时间 2023/4/6
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_goods_category")
public class CrmGoodsCategoryDO extends BaseDO {
    /**
     * 品类编码
     */
    private String code;

    /**
     * 品类名称
     */
    private String name;

    /**
     * 品类级别
     */
    private Integer categoryLevel;

    /**
     * 上级id
     */
    private Long parentId;

    /**
     * 是否末级 0：非末级，1：末级
     */
    private Integer finalStageFlag;

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
