package com.yiling.sales.assistant.task.entity;

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
 * 销售用户已选终端 
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_user_selected_terminal")
public class UserSelectedTerminalDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 销售用户主键
     */
    private Long userId;

    private Long taskId;

    /**
     * 终端主键
     */
    private Long terminalId;

    /**
     *
     */
    private Long userCustomerId;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 终端地址
     */
    private String terminalAddress;

    /**
     * 是否有已完成的交易
     */
    private Integer finishTrade;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 交易频次
     */
    private Integer tradeTimes;

    /**
     * 用户任务id
     */
    private Long userTaskId;

    /**
     * 0-占用 1-释放
     */
    private Integer released;


}
