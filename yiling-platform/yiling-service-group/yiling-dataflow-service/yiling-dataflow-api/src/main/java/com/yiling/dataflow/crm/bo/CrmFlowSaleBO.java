package com.yiling.dataflow.crm.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/9/16
 */
@Data
public class CrmFlowSaleBO implements Serializable {

    private String month;//月份
    private String department;//部门
    private String businessDepartment;//业务部门
    private String businessAreaDescription;//事业部
    private String customerCode;//药店代码
    private String customerName;//药店名称
    private String province;//省份
    private String cityCode;//城市代码
    private String city;//城市名称
    private String districtCountyCode;//区县代码
    private String districtCounty;//区县名称
    private String hospitalPharmacyType;//直营/加盟
    private String hospitalPharmacyAttribute;//连锁/单店
    private String hospitalPharmacyLevel;//药店属性
    //    private String hospitalPharmacyLevel;//药店级别
    private String chainDrugstoreSuperiorCode;//连锁上级编码
    private String chainDrugstoreSuperiorName;//连锁名称
    private String protocolConcatenationCode;//协议连锁编码
    private String protocolConcatenationName;//协议连锁名称
    private String isKa;//是否KA
    private String substituteRunning;//是否代跑
    //    流向内购进
//    流向外购进窜货批复
//    流向外购进窜货扣减
//    流向外购进反流向
//    流向外购进合计
    private String   crmGoodsId;//产品代码
    private String goodsName;//    产品名称
    private String breed;//    品种

    private BigDecimal quantity;//    购进合计
    private BigDecimal amount;//    购进总金额
    private BigDecimal price;//    单价
    private String     provincialArea;//    大区/省区
    private String     businessProvince;//    业务省区
    private String     businessArea;//    业务区域
    private String     representativeCode;//    员工工号
    private String     representativeName;//    员工姓名
    private String     superiorSupervisorCode;//    主管工号
    private String     superiorSupervisorName;//    主管姓名
    private String     productGroup;//    产品组
    private String     targetOrNot;//    是否目标

}
