package com.yiling.dataflow.wash.entity;

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
 * erp对接企业清洗计划表
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_client_wash_plan")
public class ErpClientWashPlanDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long crmEnterpriseId;
    private Long eid;

    /**
     * 对接方式
     */
    private Integer flowMode;
    private String name;

    private Long fmwcId;

    /**
     * 计划清洗时间
     */
    private Date planTime;

    /**
     * 状态：0-未发送1-已发送2-已接收
     */
    private Integer status;

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

    private String remark;


}
