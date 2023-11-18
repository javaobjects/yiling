package com.yiling.sjms.flee.entity;

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
 * 窜货申诉拓展表单
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("fleeing_goods_form_ext")
public class FleeingGoodsFormExtDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 申报类型 1-电商 2-非电商
     */
    private Integer reportType;

    /**
     * 附件
     */
    private String appendix;

    /**
     * 申诉描述
     */
    private String fleeingDescribe;

    /**
     * 确认状态：1-待提交 2-已提交
     */
    private Integer confirmStatus;

    /**
     * 确认时的备注意见
     */
    private String confirmDescribe;

    /**
     * 确认人id
     */
    private String confirmUserId;

    /**
     * 生成流向表单时间(提交清洗)
     */
    private Date submitWashTime;

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
