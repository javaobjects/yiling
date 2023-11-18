package com.yiling.mall.agreement.entity;

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
 * 协议导入任务表
 * </p>
 *
 * @author dexi.yao
 * @date 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_import_task")
public class AgreementImportTaskDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 任务编号
     */
    private String taskCode;

    /**
     * 协议主体ID（甲方）
     */
    private Long eid;

    /**
     * 主体名称
     */
    private String ename;

    /**
     * 协议客户ID（乙方）
     */
    private Long secondEid;

    /**
     * 乙方企业名称
     */
    private String secondName;

    /**
     * 第三方客户ID（丙方）
     */
    private Long thirdEid;

    /**
     * 丙方名称
     */
    private String thirdName;

    /**
     * 状态：1-待导入 2-导入成功 3-导入失败
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String errMsg;

    /**
     * 执行时间
     */
    private Date excutionTime;

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
