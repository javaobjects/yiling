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
 * 参数操作日志表
 * </p>
 *
 * @author dexi.yao
 * @date 2022-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("report_param_log")
public class ParamLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 业务数据id
     */
    private Long dataId;

    /**
     * 参数id
     */
    private Long paramId;

    /**
     * 子参数id
     */
    private Long paramSubId;

    /**
     * 参数类型：-1-价格 1-商品类型 2-促销活动 3-阶梯规则  4-会员返利
     */
    private Integer parType;

    /**
     * 日志类型：1-新增 2-修改 3-删除
     */
    private Integer logType;

    /**
     * 操作前
     */
    private String beforeData;

    /**
     * 操作后
     */
    private String afterData;

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
