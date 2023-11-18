package com.yiling.sjms.sjsp.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据审批部门领导对应关系
 * </p>
 *
 * @author dexi.yao
 * @date 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DataApproveDeptDTO extends BaseDTO {

    private static final long serialVersionUID = -3718004441329577084L;

    /**
     * 板块：1-商业 2-医疗 3-零售
     */
    private Integer plate;

    /**
     * 板块经理编码
     */
    private String plateDirId;

    /**
     * 板块经理名称
     */
    private String plateDirName;

    /**
     * 事业部编码
     */
    private String depId;

    /**
     * 事业部名称
     */
    private String depName;

    /**
     * 事业部总监编码
     */
    private String depDirId;

    /**
     * 事业部总监名称
     */
    private String depDirName;

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
