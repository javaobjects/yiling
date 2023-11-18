package com.yiling.dataflow.gb.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbAppealFormDTO extends BaseDTO {

    /**
     * 团购主表Id
     */
    private Long gbOrderId;

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
     * 流向月份
     */
    private String flowMonth;

    /**
     * 所属月份
     */
    private String matchMonth;

    /**
     * 团购月份
     */
    private String gbMonth;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 商业公司编码
     */
    private Long crmId;

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * 终端名称
     */
    private String enterpriseName;

    private Long orgCrmId;

    /**
     * 产品(sku)编码
     */
    private Long goodsCode;

    /**
     * 产品品名
     */
    private String goodsName;

    /**
     * 团购数量
     */
    private BigDecimal gbQuantity;

    /**
     * 销量计入人工号
     */
    private String sellerEmpId;

    /**
     * 销量计入人姓名
     */
    private String sellerEmpName;

    /**
     * 机构业务部门
     */
    private String businessDepartment;

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
     * 团购性质:1-普通；2-政府采购
     */
    private Integer gbNature;

    /**
     * 流向匹配条数
     */
    private Long flowMatchNumber;

    /**
     * 处理状态:1-未开始、2-自动处理中、3-已处理、4-处理失败、5-手动处理中。数据字典：gb_appeal_form_data_exec_status
     */
    private Integer dataExecStatus;

    /**
     * 处理类型：1自动2人工
     */
    private Integer execType;

    /**
     * 团购锁定类型：1锁定2非锁定
     */
    private Integer gbLockType;

    /**
     * 最后操作人
     */
    private Long lastUpdateUser;

    /**
     * 最后操作时间
     */
    private Date lastUpdateTime;


}
