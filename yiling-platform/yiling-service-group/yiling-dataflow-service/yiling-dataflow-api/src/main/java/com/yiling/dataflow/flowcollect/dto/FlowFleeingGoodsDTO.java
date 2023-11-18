package com.yiling.dataflow.flowcollect.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 窜货申报上传数据表
 * </p>
 *
 * @author yong.zhang
 * @date 2023-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowFleeingGoodsDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 上传记录ID
     */
    private Long recordId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 经销商名称
     */
    private String name;

    /**
     * 客户名称
     */
    private String enterpriseName;

    /**
     * 原始产品名称
     */
    private String goodsName;

    /**
     * 原始产品规格
     */
    private String soSpecifications;

    /**
     * 数量（盒）
     */
    private BigDecimal soQuantity;

    /**
     * 单位
     */
    private String soUnit;

    /**
     * 单价
     */
    private BigDecimal soPrice;

    /**
     * 金额
     */
    private BigDecimal soTotalAmount;

    /**
     * 业务代表工号
     */
    private String representativeCode;

    /**
     * 业务代表姓名
     */
    private String representativeName;

    /**
     * 业务部门
     */
    private String deptName;

    /**
     * 备注
     */
    private String notes;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
