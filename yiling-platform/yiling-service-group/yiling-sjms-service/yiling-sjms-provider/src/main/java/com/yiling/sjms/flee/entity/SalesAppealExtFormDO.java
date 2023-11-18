package com.yiling.sjms.flee.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import java.math.BigDecimal;
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
 * 销量申诉拓展表单
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sales_appeal_ext_form")
public class SalesAppealExtFormDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 申诉类型 1 补传月流向 2 调整月流向 3代表终端类型错误 4终端类型申诉 5其他
     */
    private Integer appealType;

    /**
     * 申诉金额
     */
    private BigDecimal appealAmount;

    /**
     * 调整月流向对应的调整事项 1漏做客户关系对照 2未备案商业销售到锁定终端3医院分院以总院名头进货4 医院的院内外药店进货5医联体、医共体共用进货名头6互联网医院无法体现医院名字7药店子公司以总部名头进货
     */
    private Integer monthAppealType;

    /**
     * 附件
     */
    private String appendix;

    /**
     * 申诉描述
     */
    private String appealDescribe;

    /**
     * 确认状态：1-待确认 2-确认提交 3-已提交/未清洗
     */
    private Integer ConfirmStatus;

    /**
     * 确认时的备注意见
     */
    private String confirmRemark;

    /**
     * 确认人id
     */
    private String confirmUser;

    /**
     * 提交清洗时间
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
