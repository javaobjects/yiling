package com.yiling.sales.assistant.task.entity;

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
 * 随货同行单匹配流向
 * </p>
 *
 * @author gxl
 * @date 2023-01-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_accompanying_bill_match")
public class AccompanyingBillMatchDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long accompanyingBillId;

    /**
     * 流向核对结果 字典accompany_bill_match_status
     */
    private Integer result;

    /**
     * erp核对结果
     */
    private Integer erpResult;

    /**
     * crm核对结果
     */
    private Integer crmResult;

    /**
     * erp流向匹配时间
     */
    private Date erpMatchTime;

    /**
     * crm流向匹配时间
     */
    private Date crmMatchTime;

    /**
     * erp发货日期
     */
    private Date erpDeliveryTime;

    /**
     * erp发货单位eid
     */
    private Long erpDistributorEid;

    /**
     * 资料第一次上传时间
     */
    private Date uploadTime;

    /**
     * erp发货单位
     */
    private String erpDistributorName;

    /**
     * erp收货单位
     */
    private String erpRecvName;

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
