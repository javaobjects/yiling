package com.yiling.dataflow.report.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/3/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowWashPharmacyPurchaseReportPageRequest extends QueryPageListRequest {
    /**
     * 年月
     */
    private String soMonth;

    /**
     *
     * 机构名称
     */
    private String name;

    /**
     * 机构编码
     */
    private Long crmId;

    /**
     * 机构省份
     */
    private String provinceName;

    /**
     * 机构城市
     */
    private String cityName;

    /**
     * 机构区县
     */
    private String regionName;

    /**
     * 机构业务省区
     */
    private String businessProvince;


    /**
     * 机构业务部门
     */
    private String businessDepartment;


    /**
     * 产品(sku)编码
     */
    private Long goodsCode;

    /**
     * 标准商品名称
     */
    private String goodsName;

    /**
     * 药店级别 1-A级；2-B级；3-C级
     */
    private Integer pharmacyLevel;

    /**
     * 药店属性 1-连锁分店；2-单体药店
     */
    private Integer pharmacyAttribute;

    /**
     * 药店类型 1-直营；2-加盟
     */
    private Integer pharmacyType;

    /**
     * 数据权限过滤字段
     */
    private List<Long> crmIdList;

    /**
     * 数据权限过滤字段
     */
    private List<String> provinceNameList;

}
