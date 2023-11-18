package com.yiling.open.monitor.entity;

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
 * @date 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("monitor_abnormal_data")
public class MonitorAbnormalDataDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商业公司ID
     */
    private Long eid;

    /**
     * 商业公司名称
     */
    private String ename;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 销售单据时间
     */
    private Date flowTime;

    /**
     * 主键ID
     */
    private String soId;

    /**
     * 销售单号
     */
    private String soNo;

    /**
     * 任务上传ID
     */
    private Long controlId;

    /**
     * 原始数据ID（新增的）
     */
    private Long parentId;

    /**
     * 异常类型：1超过3天以后上传的数据，2原始数据
     */
    private Integer dataType;

    /**
     * 操作类型：1新增 2修改（字典：erp_oper_type）
     */
    private Integer operType;

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
