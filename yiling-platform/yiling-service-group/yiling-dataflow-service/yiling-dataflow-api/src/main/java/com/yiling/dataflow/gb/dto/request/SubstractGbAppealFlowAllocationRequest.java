package com.yiling.dataflow.gb.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubstractGbAppealFlowAllocationRequest extends BaseRequest {


    /**
     * 扣减/新增明细ID
     */
    private Long id;

    /**
     * 申诉申请Id
     */
    private Long appealFormId;

    /**
     * 团购主表Id
     */
    private Long gbOrderId;

    /**
     * 关联流向表id
     */
    private Long gafrId;

    /**
     * 团购表单ID
     */
    private Long formId;

    /**
     * 团购编号
     */
    private String gbNo;

    /**
     * 所属月份
     */
    private String matchMonth;

    /**
     * 团购月份
     */
    private String gbMonth;

    /**
     * 源流向Id
     */
    private Long flowWashId;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 商业编码
     */
    private Long crmId;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 原始客户名称
     */
    private String originalEnterpriseName;

    /**
     * 机构编码
     */
    private Long customerCrmId;

    /**
     * 机构名称
     */
    private String enterpriseName;

    /**
     * 原始商品名称
     */
    private String soGoodsName;

    /**
     * 原始商品规格
     */
    private String soSpecifications;

    /**
     * 产品(sku)编码
     */
    private Long goodsCode;

    /**
     * 产品品名
     */
    private String goodsName;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 产品单价
     */
    private BigDecimal price;

    /**
     * 金额
     */
    private BigDecimal totalAmount;

    /**
     * 机构部门ID(用户自己填写部门名称时此id为0)
     */
    private Long orgId;

    /**
     * 机构部门
     */
    private String department;

    /**
     * 机构业务部门ID(用户自己填写业务部门名称时此id为0)
     */
    private Long businessOrgId;

    /**
     * 业务部门
     */
    private String businessDepartment;

    /**
     * 机构省区
     */
    private String provincialArea;

    /**
     * 机构业务省区
     */
    private String businessProvince;

    /**
     * 机构区办代码
     */
    private String districtCountyCode;

    /**
     * 机构区办
     */
    private String districtCounty;

    /**
     * 主管工号
     */
    private String superiorSupervisorCode;

    /**
     * 主管名称
     */
    private String superiorSupervisorName;

    /**
     * 代表工号
     */
    private String representativeCode;

    /**
     * 代表姓名
     */
    private String representativeName;

    /**
     * 岗位代码
     */
    private Long postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 分配类型:1-扣减；2-增加
     */
    private Integer allocationType;

    /**
     * 流向业务主键
     */
    private String flowKey;

}
