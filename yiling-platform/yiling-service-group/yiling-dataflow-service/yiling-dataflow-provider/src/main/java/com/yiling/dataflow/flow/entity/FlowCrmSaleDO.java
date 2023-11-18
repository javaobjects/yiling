package com.yiling.dataflow.flow.entity;

import java.math.BigDecimal;
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
 * @date 2023/2/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_crm_sale")
public class FlowCrmSaleDO extends BaseDO {

    private static final long serialVersionUID = -1583014332359454076L;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 商业公司名称
     */
    private String ename;

    /**
     * 流向客户名称
     */
    private String enterpriseName;

    /**
     * crm产品代码
     */
    private String crmGoodsCode;

    /**
     * crm产品名称
     */
    private String crmGoodsName;

    /**
     * 数量
     */
    private BigDecimal soQuantity;

    /**
     * 批号
     */
    private String soBatchNo;

    /**
     * 源文件名
     */
    private String sourceFile;

    /**
     * 文件导入状态 0-未导入 1-导入中 2-导入完成
     */
    private Integer status;

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
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;
}
