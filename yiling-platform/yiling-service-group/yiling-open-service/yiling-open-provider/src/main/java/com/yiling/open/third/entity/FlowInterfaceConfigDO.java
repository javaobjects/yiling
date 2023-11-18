package com.yiling.open.third.entity;

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
 * @date 2022-04-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_interface_config")
public class FlowInterfaceConfigDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商业公司编号
     */
    private Long suId;

    /**
     * 请求参数配置json
     */
    private String param;

    /**
     * 处理对象名称
     */
    private String springId;

    /**
     * 采购单字段映射
     */
    private String purchaseMapping;

    /**
     * 销售单字段映射
     */
    private String saleMapping;

    /**
     * 商品库存字段映射
     */
    private String goodsBatchMapping;

    /**
     * 流向天数
     */
    private Integer flowDay;

    /**
     * 处理状态: 0-未处理 1-处理中 2-处理完成 3-处理失败
     */
    private Integer syncStatus;

    /**
     * 执行时间，小时：0-24
     */
    private Integer taskTime;

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 任务结束时间
     */
    private Date endTime;

    /**
     * 处理日志
     */
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
