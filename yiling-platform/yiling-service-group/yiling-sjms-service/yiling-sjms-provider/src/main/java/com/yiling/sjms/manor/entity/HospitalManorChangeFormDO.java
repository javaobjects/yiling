package com.yiling.sjms.manor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 医院辖区变更表单
 * </p>
 *
 * @author gxl
 * @date 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hospital_manor_change_form")
public class HospitalManorChangeFormDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 医院id
     */
    private Long crmEnterpriseId;

    /**
     * 品种id
     */
    private Long categoryId;

    /**
     * 旧辖区id
     */
    private Long manorId;

    /**
     * 新辖区id
     */
    private Long newManorId;

    /**
     * form表主键
     */
    private Long formId;

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

    /**
     * 数据归档：1-开启 2-关闭
     */
    private Integer archiveStatus;


}
