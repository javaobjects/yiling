package com.yiling.user.esb.entity;

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
 * esb业务架构
 * </p>
 *
 * @author lun.yu
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("esb_business_organization")
public class EsbBusinessOrganizationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID
     */
    private Long orgId;

    /**
     * 上级部门ID
     */
    private Long orgPid;

    /**
     * 上级业务架构ID
     */
    private Long bzPid;

    /**
     * 打标类型：1-事业部打标 2-业务省区打标 3-区办打标
     */
    private Integer tagType;

    /**
     * 是否可以上传指标：0-否 1-是
     */
    private Integer targetStatus;

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
