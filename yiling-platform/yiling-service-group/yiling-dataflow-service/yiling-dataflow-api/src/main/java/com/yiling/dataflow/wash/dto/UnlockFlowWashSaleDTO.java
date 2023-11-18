package com.yiling.dataflow.wash.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流向销售合并报
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockFlowWashSaleDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 流向主表Id
     */
    private Long flowSaleWashId;

    /**
     * 月流向清洗任务id
     */
    private Long ufwtId;

    /**
     * 销售流向报表id
     */
    private Long flowWashSaleReportId;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 月份(实际)
     */
    private String soMonth;

    /**
     * 年份(实际)
     */
    private String soYear;

    /**
     * 月份(计入)
     */
    private String month;

    /**
     * 年份(计入)
     */
    private String year;

    /**
     * 商业编码
     */
    private Long crmId;

    /**
     * 商业名称（商家名称）
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
     * 机构是否锁定  1是 2否
     */
    private Integer isChainFlag;

    /**
     * 机构部门
     */
    private String department;

    /**
     * 机构业务部门
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

    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String regionCode;
    private String regionName;

    private Integer customerClassification;

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
     * 产品品规
     */
    private String goodsSpec;

    /**
     * 原始数量
     */
    private BigDecimal soQuantity;

    /**
     * 产品单价
     */
    private BigDecimal salesPrice;

    /**
     * 金额
     */
    private BigDecimal soTotalAmount;

    /**
     * 分配结果来源
     */
    private Integer distributionSource;

    /**
     * 规则ID
     */
    private Long unlockSaleRuleId;

    /**
     * 判断
     */
    private Integer judgment;

    /**
     * 非锁分配备注
     */
    private String ruleNotes;

    /**
     * 分配状态：1-未分配2-已分配
     */
    private Integer distributionStatus;

    /**
     * 操作状态：1-未操作2-已操作
     */
    private Integer operateStatus;

    /**
     * 商务负责人工号
     */
    private String commerceJobNumber;

    /**
     * 商务负责人
     */
    private String commerceLiablePerson;

    private Long enterpriseCersId;

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

    private String remark;
}
