package com.yiling.settlement.report.entity;

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
 * 报表操作日志表
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("report_log")
public class LogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 报表id
     */
    private Long reportId;

    /**
     * 数据id
     */
    private String dataId;

    /**
     * 日志类型：1-提交返利 2-运营确认 3-运营驳回 4-财务确认 5-财务驳回 6-调整金额 7-修改B2B订单标识 8-修改流向订单标识 9-管理员驳回 10-报表返利
     */
    private Integer type;

    /**
     * 操作值
     */
    private String opValue;

    /**
     * 操作备注
     */
    private String opRemark;

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
