package com.yiling.dataflow.relation.entity;

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
 * @author: houjie.sun
 * @date: 2022/10/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_goods_relation_edit_task")
public class FlowGoodsRelationEditTaskDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类型：1-erp流向销售同步 2-商品审核同步 3-以岭品关系修改同步
     */
    private Integer businessType;

    /**
     * 以岭品关系ID
     */
    private Long flowGoodsRelationId;

    /**
     * 商业公司eid
     */
    private Long eid;

    /**
     * 商品内码
     */
    private String flowSaleIds;

    /**
     * 同步状态：0未同步，1正在同步 2返利同步成功 4同步失败
     */
    private Integer syncStatus;

    /**
     * 同步信息
     */
    private String syncMsg;

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
