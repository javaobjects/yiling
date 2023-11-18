package com.yiling.dataflow.flowcollect.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/16 0016
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowFleeingGoodsRequest extends BaseRequest {

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
}
