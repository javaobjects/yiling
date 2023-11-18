package com.yiling.dataflow.gb.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 团购主表
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gb_order")
public class GbOrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 团购表单ID
     */
    private Long formId;

    /**
     * 团购编号
     */
    private String gbNo;

    /**
     * 所属流程：1-团购提报 2-团购取消。字典：form_type
     */
    private Integer gbProcess;

    /**
     * 团购月份
     */
    private String gbMonth;

    /**
     * 流向月份
     */
    private String flowMonth;

    /**
     * 销量计入人工号
     */
    private String sellerEmpId;

    /**
     * 销量计入人姓名
     */
    private String sellerEmpName;

    /**
     * 销量计入人区办ID
     */
    private Long sellerDeptId;

    /**
     * 销量计入人区办名称
     */
    private String sellerDeptName;

    /**
     * 团购单位ID
     */
    private Long customerId;

    /**
     * 团购单位名称
     */
    private String customerName;

    /**
     * 出库商业ID
     */
    private Long crmId;

    /**
     * 出库商业名称
     */
    private String ename;

    /**
     * 出库终端ID
     */
    private Long orgCrmId;

    /**
     * 出库终端名称
     */
    private String enterpriseName;

    /**
     * 产品编码
     */
    private Long goodsCode;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 团购数量
     */
    private BigDecimal gbQuantity;

    /**
     * 团购性质：1-普通团购 2-政府采购
     */
    private Integer gbType;

    /**
     * 审核状态：10-待提交 20-审批中 200-已通过 201-已驳回，字典：gb_form_status
     *
     * com.yiling.sjms.form.enums.FormStatusEnum
     */
    private Integer auditStatus;

    /**
     * 复核状态
     */
    private Integer checkStatus;

    /**
     * 复核意见
     */
    private String gbRemark;

    /**
     * 发起人
     */
    private String createName;

    /**
     * 提交时间
     */
    private Date createTime;

    /**
     * 数据处理:1-未处理；2-已处理
     */
    private Integer execStatus;

}
