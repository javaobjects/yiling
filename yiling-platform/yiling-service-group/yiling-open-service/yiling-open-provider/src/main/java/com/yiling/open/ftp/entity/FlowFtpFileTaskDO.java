package com.yiling.open.ftp.entity;

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
 * @author fucheng.bai
 * @date 2023/3/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_ftp_file_task")
public class FlowFtpFileTaskDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商业公司编码
     */
    private Long suId;

    /**
     * 源文件名称
     */
    private String sourceFileName;

    /**
     * 文件解析正常条数
     */
    private Integer successCount;

    /**
     * 文件解析失败条数
     */
    private Integer failCount;

    /**
     * 文件解析状态 1-未开始 2-解析中 3-解析成功 4-解析失败
     */
    private Integer analyticStatus;


    /**
     * 文件解析开始时间
     */
    private Date analyticStartTime;

    /**
     * 文件解析结束时间
     */
    private Date analyticEndTime;

    /**
     * 实施负责人
     */
    private String installEmployee;

    /**
     * 错误信息
     */
    private String errMsg;

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
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    private String remark;

}
