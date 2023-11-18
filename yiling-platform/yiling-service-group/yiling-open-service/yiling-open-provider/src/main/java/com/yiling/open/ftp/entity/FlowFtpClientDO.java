package com.yiling.open.ftp.entity;

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
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_ftp_client")
public class FlowFtpClientDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商业公司编码
     */
    private Long suId;

    /**
     * ftp用户名称
     */
    private String ftpUserName;

    /**
     * ftp用户密码
     */
    private String ftpPassWord;

    private Integer shopSaleDay;

    private Integer flowDay;

    private String ftpIp;

    private Integer ftpPort;

    private String installEmployee;

    private Integer fileType;

    private Date startTime;

    private Date endTime;

    private Integer syncStatus;

    private String syncMsg;

    private String remark;

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

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;


}
